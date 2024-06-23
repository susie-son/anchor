package com.susieson.anchor.ui.components.form

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.susieson.anchor.ui.components.LabeledItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormSelectFilterItem(
    @StringRes
    label: Int,
    filters: Map<Int, Boolean>,
    onFilterChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LabeledItem(
        label = label,
        modifier = modifier,
        isOnSameLine = false,
        color = if (filters.none {
                it.value
            }
        ) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { (filter, selected) ->
                FilterChip(
                    selected = selected,
                    onClick = { onFilterChange(filter) },
                    label = { Text(stringResource(filter)) }
                )
            }
        }
    }
}
