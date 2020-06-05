package io.github.dotslash21.pocketdictionary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.*
import io.github.dotslash21.pocketdictionary.api.Definition
import io.github.dotslash21.pocketdictionary.api.Meaning
import io.github.dotslash21.pocketdictionary.api.WordDefinition
import io.github.dotslash21.pocketdictionary.result.DictionaryApiStatus
import java.lang.reflect.Type

/*
* Adapter for wordDefinitionsRecyclerView
*/
class WordDefinitionsAdapter :
    RecyclerView.Adapter<WordDefinitionsAdapter.WordDefinitionViewHolder>() {
    var data = listOf<WordDefinition>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // Create the parent view pool to share it with the child RecyclerViews
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holderWordDefinition: WordDefinitionViewHolder, position: Int) {
        val item = data[position]
        holderWordDefinition.bind(position, item, viewPool)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordDefinitionViewHolder {
        return WordDefinitionViewHolder.from(parent)
    }

    class WordDefinitionViewHolder private constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val wordIdTextView: TextView = itemView.findViewById(R.id.wordIdTextView)
        private val wordTextView: TextView = itemView.findViewById(R.id.wordTextView)
        private val wordPhoneticTextView: TextView =
            itemView.findViewById(R.id.wordPhoneticTextView)
        private val wordOriginTextView: TextView = itemView.findViewById(R.id.wordOriginTextView)
        private val meaningsRecyclerView: RecyclerView =
            itemView.findViewById(R.id.meaningsRecyclerView)

        companion object {
            fun from(parent: ViewGroup): WordDefinitionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.word_definitions_recyclerview_row, parent, false)

                return WordDefinitionViewHolder(
                    view
                )
            }
        }

        fun bind(
            position: Int,
            item: WordDefinition,
            viewPool: RecyclerView.RecycledViewPool
        ) {
            wordIdTextView.text = wordNumberTextFormatter(position, itemView.resources)
            wordTextView.text = item.word

            // Hide the view if phonetic field is empty otherwise show it.
            if (item.phonetic.isEmpty()) {
                wordPhoneticTextView.visibility = View.GONE
            } else {
                wordPhoneticTextView.visibility = View.VISIBLE
            }
            wordPhoneticTextView.text = item.phonetic

            // Hide the view if origin field is empty otherwise show it.
            if (item.origin.isEmpty()) {
                wordOriginTextView.visibility = View.GONE
            } else {
                wordOriginTextView.visibility = View.VISIBLE
            }
            wordOriginTextView.text = originTextFormatter(item.origin, itemView.resources)

            // Create the child RecyclerView
            meaningsRecyclerView.apply {
                layoutManager = LinearLayoutManager(
                    meaningsRecyclerView.context,
                    RecyclerView.VERTICAL,
                    false
                )
                adapter = MeaningsAdapter(item.meanings, viewPool)
                setRecycledViewPool(viewPool)
            }
        }
    }
}


/*
* Adapter for meaningsRecyclerView
*/
class MeaningsAdapter(
    private val data: List<Meaning>,
    private val viewPool: RecyclerView.RecycledViewPool
) :
    RecyclerView.Adapter<MeaningsAdapter.MeaningViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holderMeaning: MeaningViewHolder, position: Int) {
        val item = data[position]
        holderMeaning.bind(item, viewPool)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningViewHolder {
        return MeaningViewHolder.from(parent)
    }

    class MeaningViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val partOfSpeechTextView: TextView =
            itemView.findViewById(R.id.partOfSpeechTextView)
        private val definitionsRecyclerView: RecyclerView =
            itemView.findViewById(R.id.definitionsRecyclerView)

        companion object {
            fun from(parent: ViewGroup): MeaningViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.meanings_recyclerview_row, parent, false)

                return MeaningViewHolder(
                    view
                )
            }
        }

        fun bind(
            item: Meaning,
            viewPool: RecyclerView.RecycledViewPool
        ) {
            partOfSpeechTextView.text = item.partOfSpeech

            // Create the child RecyclerView
            definitionsRecyclerView.apply {
                layoutManager = LinearLayoutManager(
                    definitionsRecyclerView.context,
                    RecyclerView.VERTICAL,
                    false
                )
                adapter = DefinitionsAdapter(item.definitions)
                addItemDecoration(
                    DividerItemDecoration(
                        definitionsRecyclerView.context,
                        RecyclerView.VERTICAL
                    )
                )
                setRecycledViewPool(viewPool)
            }
        }
    }
}

/*
* Adapter for definitionsRecyclerView
*/
class DefinitionsAdapter(private val data: List<Definition>) :
    RecyclerView.Adapter<DefinitionsAdapter.DefinitionViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holderDefinition: DefinitionViewHolder, position: Int) {
        val item = data[position]

        holderDefinition.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        return DefinitionViewHolder.from(parent)
    }

    class DefinitionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val definitionTextView: TextView = itemView.findViewById(R.id.definitionTextView)
        private val exampleTextView: TextView = itemView.findViewById(R.id.exampleTextView)

        companion object {
            fun from(parent: ViewGroup): DefinitionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.definitions_recyclerview_row, parent, false)

                return DefinitionViewHolder(
                    view
                )
            }
        }

        fun bind(
            item: Definition
        ) {
            definitionTextView.text = definitionTextFormatter(item.definition, itemView.resources)

            if (item.example.isEmpty()) {
                exampleTextView.visibility = View.GONE
            } else {
                exampleTextView.visibility = View.VISIBLE
            }
            exampleTextView.text = exampleTextFormatter(item.example, itemView.resources)
        }
    }
}

/*
*   Adapter to skip bad objects in a list.
*   Credit: https://stackoverflow.com/a/54152127
*/
class SkipBadListObjectsAdapterFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        return if (annotations.isEmpty() && Types.getRawType(type) == List::class.java) {
            val elementType = Types.collectionElementType(type, List::class.java)
            val elementAdapter = moshi.adapter<Any>(elementType)

            SkipBadListObjectsAdapter(elementAdapter)
        } else {
            null
        }
    }

    private class SkipBadListObjectsAdapter<T : Any>(private val elementAdapter: JsonAdapter<T>) :
        JsonAdapter<List<T>>() {
        override fun fromJson(reader: JsonReader): List<T>? {
            val goodObjectsList = mutableListOf<T>()

            reader.beginArray()

            while (reader.hasNext()) {
                try {
                    elementAdapter.fromJson(reader)?.let(goodObjectsList::add)
                } catch (e: JsonDataException) {
                    // Skip bad element ;)
                }
            }

            reader.endArray()

            return goodObjectsList

        }

        override fun toJson(writer: JsonWriter, value: List<T>?) {
            throw UnsupportedOperationException("SkipBadListObjectsAdapter is only used to deserialize objects")
        }
    }
}

/*
* Binding adapter to bind with statusImageView@fragment_search_result
*/
@BindingAdapter("dictionaryApiStatus")
fun bindStatus(
    statusImageView: ImageView,
    status: DictionaryApiStatus?
) {
    when (status) {
        // Show loading spinner
        DictionaryApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }

        // Show network error
        DictionaryApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }

        // Hide the ImageView
        DictionaryApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}