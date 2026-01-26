<template>
  <div class="flex-1">
    <h2 class="text-2xl font-bold text-center m-5">Issue report</h2>
    <DataView
      :value="filteredIssues"
      responsive-layout="scroll"
      :sort-field="sortField"
      :sort-order="sortOrder"
      paginator
      :rows="10"
      :rows-per-page-options="[10, 20, 50]"
      class="mx-5 my-3"
    >
      <template #header v-if="issues.length > 5">
        <span class="mr-2">Sources:</span>
        <MultiSelect
          v-model="selectedSources"
          :options="sources"
          optionLabel="label"
          placeholder="All"
        />
        <span class="ml-4 mr-2">File types:</span>
        <MultiSelect
          v-model="selectedFileTypes"
          :options="fileTypes"
          optionLabel="label"
          placeholder="All"
        />
        <span class="ml-4 mr-2">Sort by:</span>
        <Select
          v-model="sortKey"
          :options="sortOptions"
          optionLabel="label"
          @change="onSortChange($event)"
        />
      </template>
      <template #list="slotProps">
        <div class="flex flex-col divide-y divide-gray-500">
          <!-- TODO: use different key than index -->
          <div
            v-for="(item, index) in slotProps.items"
            :key="index"
            class="p-2"
          >
            <div class="flex items-center gap-2 mb-2">
              <div class="w-5 h-5" v-html="getIcon(item.component).svg" />
              <div class="flex flex-col">
                <span>{{ item.file }}</span>
                <span class="text-xs text-gray-500">{{ item.location }}</span>
              </div>
            </div>
            <div>
              <a
                :href="item.ruleDocLink"
                target="_blank"
                class="text-blue-600 hover:underline"
                >{{ item.source }} - {{ item.rule }}</a
              >
              <Chip
                v-for="tag in item.tags"
                :key="tag"
                :label="tag"
                class="ml-2"
              />
            </div>
            <div>
              {{ item.message }}
            </div>
            <div class="mt-2 mb-1">
              <Chip v-if="item.severity" class="m-1" :label="item.severity" />
              <Chip v-if="item.type" class="m-1" :label="item.type" />
              <Chip
                v-if="item.impacts.softwareQuality"
                class="m-1"
                :label="item.impacts.softwareQuality"
              />
              <Chip
                v-if="item.impacts.severity"
                class="m-1"
                :label="item.impacts.severity"
              />
              <Chip v-if="item.quickFixAvailable" class="m-1" label="QF" />
              <Chip v-if="item.effort" class="m-1" :label="item.effort" />
            </div>
          </div>
        </div>
      </template>
    </DataView>
  </div>
</template>

<script setup lang="ts">
import { getIcon } from "material-file-icons";

import type { AuditIssue } from "~/domain/audit-issue";

const { issues } = defineProps({
  issues: {
    type: Array<AuditIssue>,
    required: true,
  },
});

// --- FILTERING ---
const selectedSources = ref<{ label: string, value: string }[]>([])
const sources = computed(() => {
    return [...new Set(issues.map((issue) => issue.source))]
        .map(source => {
            return { label: source, value: source }
        })
})

const selectedFileTypes = ref<{ label: string, value: string }[]>([])
const fileTypes = computed(() => {
    return [...new Set(issues.map((issue) => issue.fileType))]
        .map(fileType => {
            return { label: fileType || '<empty>', value: fileType }
        })
        .sort((a, b) => a.label.localeCompare(b.label))
})

const filterBySelectedSources = (issues: AuditIssue[]) => {
    if (selectedSources.value.length === 0) {
        return issues
    }
    return issues.filter((issue) => selectedSources.value.some(source => source.value === issue.source))
}

const filterBySelectedFileTypes = (issues: AuditIssue[]) => {
    if (selectedFileTypes.value.length === 0) {
        return issues
    }
    return issues.filter((issue) => selectedFileTypes.value.some(fileType => fileType.value === issue.fileType))
}

const filteredIssues = computed(() => {
    return filterBySelectedFileTypes(filterBySelectedSources(issues))
})

// --- SORTING ---
const getFullPath = (issue: AuditIssue) => {
  return `${issue.location}/${issue.file}`
}

const sortOptions = ref([
    { label: 'Full Path', value: 'fullPath' },
    { label: 'Severity', value: 'severity'},
    { label: 'Effort', value: 'effort'},
]);
const sortKey = ref(sortOptions.value[0])
const sortOrder = ref(1)
const sortField = ref(getFullPath)

const onSortChange = (event) => {
    const value = event.value.value
    const sortValue = event.value
    sortOrder.value = 1

    if (value === 'fullPath') {
        sortField.value = getFullPath
    } else {
        sortField.value = value
    }
    sortKey.value = sortValue
};
</script>
