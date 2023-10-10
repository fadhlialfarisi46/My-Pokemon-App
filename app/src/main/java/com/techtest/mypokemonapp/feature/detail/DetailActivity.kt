package com.techtest.mypokemonapp.feature.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.techtest.mypokemonapp.R
import com.techtest.mypokemonapp.core.data.remote.response.AbilitiesItem
import com.techtest.mypokemonapp.core.data.remote.response.DetailPokemonResponse
import com.techtest.mypokemonapp.core.utils.Constant.EXTRA_ID
import com.techtest.mypokemonapp.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = getIdFromIntent()
        getDetailPokemon(id)
    }

    private fun getDetailPokemon(id: String) {
        viewModel.getDetailPokemon(id).observe(this) { result ->
            result.onSuccess { data ->
                bindDatatoView(data)
            }
            result.onFailure {
                it.localizedMessage?.let { errorMessage -> Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show() }
            }
        }
    }

    private fun bindDatatoView(data: DetailPokemonResponse) {
        binding.apply {
            progressBar.isVisible = false
            groupDetailView.isVisible = true

            tvName.text = data.name

            Glide.with(this@DetailActivity)
                .load(data.sprites.frontDefault)
                .into(ivPokemon)

            setChipAbility(data.abilities)
        }
    }

    private fun setChipAbility(abilities: List<AbilitiesItem>) {
        abilities.forEach {
            val chip = layoutInflater.inflate(R.layout.item_chip_ability, binding.chipGroup, false) as Chip
            chip.text = it.ability.name
            chip.isClickable = false
            chip.isCheckable = false
            binding.chipGroup.addView(chip)
        }
    }

    private fun getIdFromIntent(): String {
        return intent.getStringExtra(EXTRA_ID) ?: ""
    }
}
