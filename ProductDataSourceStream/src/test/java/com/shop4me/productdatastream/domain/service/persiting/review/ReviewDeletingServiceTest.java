package com.shop4me.productdatastream.domain.service.persiting.review;

import com.shop4me.productdatastream.domain.model.data.dto.Review;
import com.shop4me.productdatastream.domain.port.requesting.ReviewDeleteRequest;
import com.shop4me.productdatastream.domain.service.persisting.review.ReviewDeletingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static com.shop4me.productdatastream.domain.model.data.ReviewEntityJpqlRepository.REVIEW_DELETE_JPQL;
import static com.shop4me.productdatastream.domain.model.request.ReviewOperationRequestTestImpl.reviewDeleteRequest;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ReviewDeletingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Captor
    private ArgumentCaptor<String> jpqlCaptor;

    private final ReviewDeleteRequest request = reviewDeleteRequest(
            new Review(1L, 1L, 1L, null, (byte) 10, null, null)
    );

    @Test
    void shouldExecuteJpqlQueryCreatedFromReviewDeleteRequest(){
        given(entityManager.createQuery(REVIEW_DELETE_JPQL))
                .willReturn(query);

        reviewDeletingService().execute(request);

        then(entityManager).should().createQuery(jpqlCaptor.capture());

        assertThat(jpqlCaptor.getValue()).isEqualTo(REVIEW_DELETE_JPQL);
    }

    @Test
    void shouldBeDoneExecutionOfJpqlWrittenByRequest(){
        given(entityManager.createQuery(REVIEW_DELETE_JPQL))
                .willReturn(query);

        reviewDeletingService().execute(request);

        then(query).should().executeUpdate();
    }

    @Test
    void shouldReturnMapOfAffectedRows(){
        given(entityManager.createQuery(REVIEW_DELETE_JPQL))
                .willReturn(query);
        given(query.executeUpdate()).willReturn(1);

        var affectedRowsMap = reviewDeletingService().execute(request);

        assertThat(affectedRowsMap).containsOnly(entry("affected_rows", 1));
    }

    @Test
    void shouldThrowNullPointerExcIfNullRequest(){
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(()->{
            reviewDeletingService().execute(null);
        });
    }

    private ReviewDeletingService reviewDeletingService(){
        return new ReviewDeletingService(entityManager);
    }
}
