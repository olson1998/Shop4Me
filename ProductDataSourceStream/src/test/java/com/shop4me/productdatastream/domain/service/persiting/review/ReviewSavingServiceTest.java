package com.shop4me.productdatastream.domain.service.persiting.review;

import com.shop4me.productdatastream.domain.model.data.dto.Review;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ReviewEntity;
import com.shop4me.productdatastream.domain.port.persisting.dao.ReviewDao;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ReviewDto;
import com.shop4me.productdatastream.domain.port.requesting.ReviewSaveRequest;
import com.shop4me.productdatastream.domain.service.persisting.review.ReviewSavingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.shop4me.productdatastream.domain.model.request.ReviewOperationRequestTestImpl.reviewSaveRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewSavingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Captor
    private ArgumentCaptor<ReviewDao> reviewDaoCaptor;

    private final ReviewSaveRequest request = reviewSaveRequest(
            writeReviewSaveMap(
                    new Review(null, 1L, 1L, null, (byte) 10, null, null),
                    new Review(null, 2L, 2L, null, (byte) 1, null, null),
                    new Review(null, 3L, 3L, null, (byte) 3, null, null),
                    new Review(null, 4L, 4L, null, (byte) 5, null, null)
            )
    );

    @Test
    void shouldPersistReviewEntityIfNoExceptionThrown(){
        var requestedReviewDaoCollToSave = request.getRequestedToSave().values();
        var reviewsToSaveQty = requestedReviewDaoCollToSave.size();

        reviewSavingService().execute(request);

        then(entityManager).should(times(reviewsToSaveQty)).persist(reviewDaoCaptor.capture());

        var requestedReviewsToSave = requestedReviewDaoCollToSave.stream()
                .map(ReviewDto::toDao)
                .map(ReviewDao::toString)
                .toList();

        var capturedReviewDaoList = reviewDaoCaptor.getAllValues().stream()
                .map(ReviewDao::toString)
                .toList();

        assertThat(capturedReviewDaoList).containsAll(requestedReviewsToSave);
    }

    @Test
    void shouldNotPersistEntityIfExceptionOccurred(){
        doThrow(EntityExistsException.class).when(entityManager)
                .persist(any(ReviewEntity.class));

        reviewSavingService().execute(request);

        then(entityManager).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldReturnMapOfCorrelationIdAndSuccessIfSuccessfullyPersistTheReviewEntity(){
        var executionStatusMap = reviewSavingService().execute(request);

        then(entityManager).should(atLeastOnce()).persist(any(ReviewEntity.class));

        assertThat(executionStatusMap.values())
                .allMatch(status -> status.equals("SUCCESS"));
    }

    @Test
    void shouldReturnMapOfCorrelationIdAndFailureIfFailedPersistTheReviewEntity(){
        doThrow(EntityExistsException.class).when(entityManager)
                .persist(any(ReviewEntity.class));

        var executionStatusMap = reviewSavingService().execute(request);

        assertThat(executionStatusMap.values())
                .allMatch(status -> status.equals("FAILURE"));
    }

    private Map<String, ReviewDto> writeReviewSaveMap(Review... reviews){
        Map<String, ReviewDto> reviewSaveMap = new HashMap<>();
        Arrays.stream(reviews).forEach(review -> {
            reviewSaveMap.put(UUID.randomUUID().toString(), review);
        });
        return  reviewSaveMap;
    }

    private ReviewSavingService reviewSavingService(){
        return new ReviewSavingService(entityManager);
    }
}
