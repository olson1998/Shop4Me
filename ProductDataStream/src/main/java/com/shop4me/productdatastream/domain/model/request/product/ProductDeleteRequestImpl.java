package com.shop4me.productdatastream.domain.model.request.product;

import com.shop4me.productdatastream.domain.model.data.dto.Product;
import com.shop4me.productdatastream.domain.model.request.toolset.RequestPayloadReader;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductDto;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.ProductDeleteRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@AllArgsConstructor

public class ProductDeleteRequestImpl implements ProductDeleteRequest {

    @Getter
    private ProductDto product;

    @Override
    public String writeJpqlQuery(){
        return  "delete from ProductEntity p where p.id= "+product.getId() +
                " and p.name= '" + product.getName()+"'";
    }

    @Override
    public String toString() {
        return "DELETE PRODUCT WHERE: (ID LIKE: '"+ product.getId() + "', " +
                "PRODUCT NAME LIKE: '" + product.getName() + "')";
    }

    @SneakyThrows
    public static ProductDeleteRequestImpl fromCoreRequest(CoreRequest request){
        var payload = request.decodePayload().getPayload();

        EmptyPayloadCheck.scan(payload, "{}");

        var productToDelete = RequestPayloadReader.OBJECT_MAPPER
                .readValue(payload, Product.class);

        return new ProductDeleteRequestImpl(productToDelete);
    }
}
