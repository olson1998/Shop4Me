package com.shop4me.productdatastream.domain.service.persiting.review;

import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.review.ReviewProperty;
import com.shop4me.productdatastream.domain.service.persisting.review.ReviewEditingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Map;

import static com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.review.ReviewProperty.*;
import static com.shop4me.productdatastream.domain.model.request.ReviewOperationRequestTestImpl.reviewEditRequest;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class ReviewEditingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Captor
    private ArgumentCaptor<String> jpqlCaptor;

    private final Map<ReviewProperty, String> reviewPropertyStringMap = Map.ofEntries(
            entry(ID, "1"),
            entry(REVIEWER_NAME, "Overwritten Name"),
            entry(TEXT, "Say, Hi!")
    );

    private final String REVIEW_EDIT_JPQL=
            "update ReviewEntity r set r.reviewerName='Overwritten Name', r.text='Say, Hi!' where r.id=1";

    @Test
    void shouldExecuteJpqlWrittenByRequest(){
        given(entityManager.createQuery(anyString()))
                .willReturn(query);

        reviewEditingService().execute(reviewEditRequest(reviewPropertyStringMap));

        then(entityManager).should().createQuery(jpqlCaptor.capture());

        assertThat(jpqlCaptor.getValue()).isEqualTo(REVIEW_EDIT_JPQL);
    }

    @Test
    void shouldExecuteUpdateFromGivenQuery(){
        given(entityManager.createQuery(REVIEW_EDIT_JPQL))
                .willReturn(query);

        reviewEditingService().execute(reviewEditRequest(reviewPropertyStringMap));

        then(query).should().executeUpdate();
    }

    @Test
    void shouldReturnMapWithEntryContainingNumberOfAffectedRows(){
        given(entityManager.createQuery(REVIEW_EDIT_JPQL))
                .willReturn(query);
        given(query.executeUpdate())
                .willReturn(1);

        var affectedRowsMap =reviewEditingService()
                .execute(reviewEditRequest(reviewPropertyStringMap));

        assertThat(affectedRowsMap).containsOnly(entry("affected_rows", 1));
    }

    private ReviewEditingService reviewEditingService(){
        return new ReviewEditingService(entityManager);
    }
}
