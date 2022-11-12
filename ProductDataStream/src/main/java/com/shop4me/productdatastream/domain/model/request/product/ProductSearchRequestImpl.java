package com.shop4me.productdatastream.domain.model.request.product;

import com.shop4me.productdatastream.domain.model.request.enumset.Operator;
import com.shop4me.productdatastream.domain.model.request.product.utils.ProductSearchFilter;
import com.shop4me.productdatastream.domain.model.request.utils.RequestPayloadReader;
import com.shop4me.productdatastream.domain.model.exception.EmptyValueException;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.model.request.product.utils.ProductSearchFilterComparator;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.objects.dto.ProductSearchFilterDto;
import com.shop4me.productdatastream.domain.port.requesting.ProductSearchRequest;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty.CATEGORY;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSearchRequestImpl implements ProductSearchRequest {

    private final ProductSearchFilter[] filters;

    private final int tenantId;

    @Override
    public String writeJpqlQuery(){
        var query = new StringBuilder("select distinct p.id from ProductEntity p ");
        var samePropAndOperatorSet =
                collectSamePropertyAndOperationCombinations();
        var combinationsList = writeCombinationsList(samePropAndOperatorSet);
        leftOuterJoinIfCategoryFilterPresent(query);
        writeJpqlWhereClauses(query, combinationsList);
        query.append("and p.tenantId=").append(tenantId);
        return query.toString();
    }

    @Override
    public String toString() {
        var str = new StringBuilder("SEARCH PRODUCT ID WHERE: (");
        var samePropAndOperatorSet =
                collectSamePropertyAndOperationCombinations();

        var combinationsList = writeCombinationsList(samePropAndOperatorSet);
        writeCombinationsListString(str, combinationsList);
        str.append(")");
        return str.toString();
    }

    private void writeJpqlWhereClauses(StringBuilder query, @NonNull List<ProductSearchFilter> combinations){
        var combinationsQty = combinations.size();
        if(combinations.size()>0){
            for(int i =0; i < combinationsQty; i++){
                var combination = combinations.get(i);
                writeJpqlWhere(query, combination);
                if(i < combinationsQty-1){
                    query.append("and ");
                }
            }
        }
    }

    private void writeJpqlWhere(StringBuilder query, @NonNull ProductSearchFilter combination){
        var sameFilters = collectFiltersWithSameCombination(combination);
        var size = sameFilters.size();
        var last = size -1;
        query.append("(");
        for(int i =0; i < size; i++){
            var filter = sameFilters.get(i);
            query.append(filter.where());
            if(i < last){
                query.append("or ");
            }
        }
        query.append(") ");
    }

    private List<ProductSearchFilter> writeCombinationsList(Set<ProductSearchFilter> sameCombinationsSet){
        var filterComparator = new ProductSearchFilterComparator();
        return List.copyOf(sameCombinationsSet).stream()
                .sorted(filterComparator)
                .collect(Collectors.toList());
    }

    private void writeCombinationsListString(StringBuilder str, @NonNull List<ProductSearchFilter> combinationsList){
        var combinationsQty = combinationsList.size();
        for(int i =0 ; i < combinationsQty; i++){
            var combination = combinationsList.get(i);
            writeCombinationWithValue(str, combination);
            if(i < combinationsQty -1){
                str.append(", ");
            }
        }
    }

    private Set<ProductSearchFilter> collectSamePropertyAndOperationCombinations(){
        var samePropAndOperatorSet = new HashSet<ProductSearchFilter>();
        Arrays.asList(filters).forEach(filter->{
            var productProperty = ProductProperty.valueOf(filter.getProperty());
            var operator = Operator.valueOf(filter.getOperator());

            var sameCombination = new ProductSearchFilter(
                    productProperty,
                    operator,
                    null
            );
            samePropAndOperatorSet.add(sameCombination);
        });
        return samePropAndOperatorSet;
    }

    private void writeCombinationWithValue(StringBuilder str, @NonNull ProductSearchFilter combination){
        var values = collectValuesOfFiltersWithSameCombination(combination);
        str.append(combination.getProperty()).append(" ").append(combination.getOperator()).append(": ");
        var size = values.size();
        if(size > 1){
            str.append("(");
            var last = size -1;
            for(int i =0; i < size; i++){
                str.append("'").append(values.get(i)).append("'");
                if(i < last){
                    str.append(", ");
                }
            }
            str.append(")");
        }
        else{
            str.append("'").append(values.get(0)).append("'");
        }
    }

    private List<ProductSearchFilter> collectFiltersWithSameCombination(ProductSearchFilter combination){
        return Arrays.stream(filters)
                .filter(filter-> filter.getProperty().equals(combination.getProperty())&&
                        filter.getOperator().equals(combination.getOperator()))
                .toList();
    }

    private List<String> collectValuesOfFiltersWithSameCombination(ProductSearchFilter combination){
        return Arrays.stream(filters)
                .filter(filter-> filter.getProperty().equals(combination.getProperty())&&
                        filter.getOperator().equals(combination.getOperator()))
                .map(ProductSearchFilterDto::getValue)
                .collect(Collectors.toList());
    }

    private void leftOuterJoinIfCategoryFilterPresent(StringBuilder query){
        Arrays.stream(filters)
                .filter(filter -> filter.getProperty().equals(CATEGORY.name()))
                .findFirst()
                .ifPresentOrElse(
                        f-> query.append("left outer join p.categoriesSet c where "),
                        ()-> query.append("where ")
                );
    }


    @SneakyThrows
    public static ProductSearchRequestImpl fromInboundMessage(@NonNull InboundMsg inboundMsg){
        var json = inboundMsg.getDecodedPayload();

        EmptyPayloadCheck.scan(json, "[]");

        var filters = RequestPayloadReader.OBJECT_MAPPER.readValue(json, ProductSearchFilter[].class);
        var comparator = new ProductSearchFilterComparator();

        throwEmptyValueExceptionIfNoValueSetForFilter(filters);
        Arrays.sort(filters, comparator);
        return new ProductSearchRequestImpl(filters, inboundMsg.getTenantId());
    }

    private static void throwEmptyValueExceptionIfNoValueSetForFilter(ProductSearchFilter[] requested){
        var corruptedFilters = new ArrayList<ProductSearchFilter>();
        Arrays.stream(requested).forEach(filter->{
            if(Objects.equals(filter.getValue(), "") || filter.getValue() == null){
                corruptedFilters.add(filter);
            }
        });
        if(corruptedFilters.size() > 0){
            var msg = corruptedFiltersMsg(corruptedFilters);
            throw new EmptyValueException("reason, no value set for: "+msg);
        }
    }

    private static String corruptedFiltersMsg(List<ProductSearchFilter> corruptedFilters){
        StringBuilder sb = new StringBuilder("(");
        var size = corruptedFilters.size();
        var last = size - 1;
        for(int i = 0; i < size; i++){
            var filter = corruptedFilters.get(i);
            sb.append(filter.getProperty())
                    .append(" ")
                    .append(filter.getOperator())
                    .append("= '?'");
            if(i < last){
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
