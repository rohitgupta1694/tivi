/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.banes.chris.tivi.home.discover

import android.view.View
import com.airbnb.epoxy.EpoxyController
import me.banes.chris.tivi.R
import me.banes.chris.tivi.data.Entry
import me.banes.chris.tivi.data.entities.ListItem
import me.banes.chris.tivi.data.entities.PopularEntry
import me.banes.chris.tivi.data.entities.TrendingEntry
import me.banes.chris.tivi.emptyState
import me.banes.chris.tivi.header
import me.banes.chris.tivi.posterGridItem
import me.banes.chris.tivi.tmdb.TmdbImageUrlProvider
import me.banes.chris.tivi.ui.epoxy.EpoxyModelProperty
import me.banes.chris.tivi.ui.epoxy.TotalSpanOverride

class DiscoverEpoxyController(
    private val callbacks: Callbacks
) : EpoxyController() {
    var trendingItems: List<ListItem<TrendingEntry>>? by EpoxyModelProperty()
    var popularItems: List<ListItem<PopularEntry>>? by EpoxyModelProperty()
    var tmdbImageUrlProvider: TmdbImageUrlProvider? by EpoxyModelProperty()

    interface Callbacks {
        fun onTrendingHeaderClicked(items: List<ListItem<TrendingEntry>>?)
        fun onPopularHeaderClicked(items: List<ListItem<PopularEntry>>?)
        fun onItemClicked(item: ListItem<out Entry>)
    }

    override fun buildModels() {
        header {
            id("trending_header")
            title(R.string.discover_trending)
            spanSizeOverride(TotalSpanOverride)
            buttonClickListener(View.OnClickListener {
                callbacks.onTrendingHeaderClicked(trendingItems)
            })
        }

        val trending = trendingItems
        if (trending != null && trending.isNotEmpty()) {
            trending.take(spanCount * 2).forEach { item ->
                posterGridItem {
                    id(item.generateStableId())
                    tmdbImageUrlProvider(tmdbImageUrlProvider)
                    posterPath(item.show?.tmdbPosterPath)
                    title(item.show?.title)
                    annotationLabel(item.entry?.watchers.toString())
                    annotationIcon(R.drawable.ic_eye_12dp)
                    transitionName("trending_${item.show?.homepage}")
                    clickListener(View.OnClickListener {
                        callbacks.onItemClicked(item)
                    })
                }
            }
        } else {
            emptyState {
                id("trending_placeholder")
                spanSizeOverride(TotalSpanOverride)
            }
        }

        header {
            id("popular_header")
            title(R.string.discover_popular)
            spanSizeOverride(TotalSpanOverride)
            buttonClickListener(View.OnClickListener {
                callbacks.onPopularHeaderClicked(popularItems)
            })
        }
        val popular = popularItems
        if (popular != null && popular.isNotEmpty()) {
            popular.take(spanCount * 2).forEach { item ->
                posterGridItem {
                    id(item.generateStableId())
                    tmdbImageUrlProvider(tmdbImageUrlProvider)
                    posterPath(item.show?.tmdbPosterPath)
                    title(item.show?.title)
                    transitionName("popular_${item.show?.homepage}")
                    clickListener(View.OnClickListener {
                        callbacks.onItemClicked(item)
                    })
                }
            }
        } else {
            emptyState {
                id("popular_placeholder")
                spanSizeOverride(TotalSpanOverride)
            }
        }
    }
}