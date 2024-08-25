import android.os.Parcelable
import com.dpm.presentation.scheme.viewmodel.SchemeState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewData(
    val stadiumId: Int,
    val blockCode: String,
    val reviewId: Int,
    val blockId: Int,
    val selectedColumn: String,
    val selectedNumber: String,
    val preSignedUrlImages: List<String>,
    val selectedGoodReview: List<String>,
    val selectedBadReview: List<String>,
    val detailReviewText: String,
    val selectedDate: String,
) : Parcelable

fun ReviewData.toNavReviewDetail() = SchemeState.NavReviewDetail(
    stadiumId = stadiumId,
    blockCode = blockCode,
    reviewId = reviewId
)
