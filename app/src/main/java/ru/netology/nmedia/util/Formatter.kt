package ru.netology.nmedia.util

fun formatCount(count: Int): String {
    return when {
        count < 1_000 -> count.toString()
        count < 10_000 -> "${String.format("%.1f", count / 1000f)}K".replace(",", ".")
        count < 1_000_000 -> "${count / 1000}K"
        else -> "${String.format("%.1f", count / 1_000_000f)}M".replace(",", ".")
    }
}