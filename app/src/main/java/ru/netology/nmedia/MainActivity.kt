package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.formatCount

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likes = 999999,
            shares = 999,
            likeByMe = false
        )

        with(binding) {
            // Установка данных поста
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = formatCount(post.likes)
            repostCount.text = formatCount(post.shares)


            // Установка иконки лайка в зависимости от состояния
            like24.setImageResource(
                if (post.likeByMe) R.drawable.ic_liked_24
                else R.drawable.ic_like_24
            )

            // Обработчик клика по лайку
            like24.setOnClickListener {
                post = if (post.likeByMe) {
                    // Убираем лайк
                    post.copy(likeByMe = false, likes = post.likes - 1)
                } else {
                    // Ставим лайк
                    post.copy(likeByMe = true, likes = post.likes + 1)
                }
                // Обновляем UI
                like24.setImageResource(
                    if (post.likeByMe) R.drawable.ic_liked_24
                    else R.drawable.ic_like_24
                )
                likeCount.text = formatCount(post.likes)
            }

            // Обработчик клика по репосту
            share.setOnClickListener {
                post = post.copy(shares = post.shares + 1)
                repostCount.text = formatCount(post.shares)
            }
        }
    }
}