package com.dpm.presentation.viewfinder.sample

val stadiums = listOf(
    Stadium(
        1, "서울", listOf("LG", "두산"), "https://picsum.photos/600/400", false,
    ),
    Stadium(
        2, "서울 잠실야구장", listOf("삼성"), "https://picsum.photos/600/400", true,
    ),
    Stadium(
        3, "서울 잠실야구장", listOf("KIA"), "https://picsum.photos/600/400", true,
    ),
    Stadium(
        4, "서울 잠실야구장", listOf("SSG"), "https://picsum.photos/600/400", true,
    ),
    Stadium(
        5, "서울 잠실야구장", listOf("히어로즈"), "https://picsum.photos/600/400", true,
    ),
    Stadium(
        6, "서울 잠실야구장", listOf("kt"), "https://picsum.photos/600/400", true,
    ),
    Stadium(
        7, "서울 잠실야구장", listOf("롯데", "한화"), "https://picsum.photos/600/400", true,
    )
)

val pictures = listOf<String>(
    "https://picsum.photos/600/400",
    "https://picsum.photos/600/400",
    "https://picsum.photos/600/400",
    "https://picsum.photos/200/300",
    "https://picsum.photos/200/300"
)

val keywords = listOf<Keyword>(
    Keyword("🙍‍서서 응원하는 존", 44, 0),
    Keyword("☀️ 온종일 햇빛 존", 44, 1),
    Keyword("🙍‍서서 응원하는 존", 44, 1),
    Keyword("🙍‍서서 응원하는 존", 44, 0),
    Keyword("🙍‍서서 응원하는 존", 44, 0)
)

val reviewContents = listOf<ReviewContent>(
    ReviewContent(
        "https://picsum.photos/600/400",
        "잠실의 주인은 엘지",
        2,
        "207블럭 1열 12번",
        "7월 7일",
        pictures,
        "제일 앞열이라 시야는 아주 좋았어요. 다만, 앞쪽으로 사람들이 많이 다니면 조금 신경 쓰일 수 있어요",
        keywords
    ),
    ReviewContent(
        "https://picsum.photos/600/400",
        "잠실의 주인은 기아",
        1,
        "117블럭 2열 34번",
        "7월 7일",
        pictures,
        "제일 앞열이라 시야는 아주 좋았어요. 다만, 앞쪽으로 사람들이 많이 다니면 조금 신경 쓰일 수 있어요.제일 앞열이라 시야는 아주 좋았어요. 다만, 앞쪽으로 사람들이 많이 다니면 조금 신경 쓰일 수 있어요",
        keywords
    ),
    ReviewContent(
        "https://picsum.photos/600/400",
        "잠실의 주인은 한화",
        3,
        "207블럭 1열 12번",
        "7월 7일",
        pictures,
        "제일 앞열이라 시야는 아주 좋았어요. 다만, 앞쪽으로 사람들이 많이 다니면 조금 신경 쓰일 수 있어요",
        keywords
    ),
    ReviewContent(
        "https://picsum.photos/600/400",
        "잠실의 주인은 엘지",
        2,
        "207블럭 1열 12번",
        "7월 7일",
        pictures,
        "제일 앞열이라 시야는 아주 좋았어요. 다만, 앞쪽으로 사람들이 많이 다니면 조금 신경 쓰일 수 있어요",
        keywords
    )
)

val review = Review(
    count = 100,
    reviewContents = reviewContents
)
