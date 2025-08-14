package ru.netology.nmedia.util

fun formatCount(count: Int): String {
    return when {
        count < 1_000 -> {
            count.toString()
        }

        count < 10_000 -> {
            val thousands = count / 1000f
            val truncated = (thousands * 10).toInt() / 10.0
            "${"%.1f".format(truncated)}K"
        }

        count < 1_000_000 -> {
            "${count / 1000}K"
        }

        else -> {
            val millions = count / 1_000_000f
            "${"%.1f".format(millions)}M"
        }
    }
}
