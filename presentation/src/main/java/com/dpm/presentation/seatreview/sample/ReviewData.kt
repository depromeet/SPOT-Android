import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewData(
    val blockId: Int,
    val selectedColumn: String,
    val selectedNumber: String,
    val preSignedUrlImages: List<String>,
    val selectedGoodReview: List<String>,
    val selectedBadReview: List<String>,
    val detailReviewText: String,
    val selectedDate: String,
) : Parcelable
