<template>
  <div class="flex-1">
    <h2 class="text-2xl font-bold text-center m-5">Issue report</h2>
    <DataView
      :value="filteredIssues"
      responsive-layout="scroll"
      :sort-field="sortFieldOrGetter"
      :sort-order="sortOrder"
      paginator
      :rows="10"
      :rows-per-page-options="[10, 20, 50]"
      class="mx-5 my-3"
    >
      <template #header v-if="issues.length > 5">
        <div class="flex flex-col sm:flex-row gap-2">
          <div class="flex items-center gap-2" v-if="tools.length > 1">
            <span>Tools:</span>
            <MultiSelect
              v-model="selectedTools"
              :options="tools"
              optionLabel="label"
              placeholder="All"
            />
          </div>
          <div class="flex items-center gap-2" v-if="fileTypes.length > 1">
            <span>File types:</span>
            <MultiSelect
              v-model="selectedFileTypes"
              :options="fileTypes"
              optionLabel="label"
              placeholder="All"
            />
          </div>
          <div class="flex items-center gap-2">
            <span>Sort by:</span>
            <Select
              v-model="sortSelectModel"
              :options="sortOptions"
              optionLabel="label"
              @change="onSortChange($event)"
            />
          </div>
          <div class="flex items-center gap-2 pl-2">
            Showing {{ filteredIssues.length }} out of {{ issues.length }}
          </div>
        </div>
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
              <div class="w-5 h-5" v-html="getIcon(item.file).svg" />
              <div class="flex flex-col">
                <span>{{ item.file }}:{{ item.line }}</span>
                <span class="text-xs text-gray-500">{{ item.location }}</span>
              </div>
            </div>
            <div>
              <a
                v-if="item.ruleDocLink"
                :href="item.ruleDocLink"
                target="_blank"
                class="text-blue-600 hover:underline"
                >{{ item.tool }} - {{ item.rule }}</a
              >
              <span v-else class="text-gray-500">{{ item.tool }} - {{ item.rule }}</span>
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
              <Chip v-if="item.issueType" class="m-1" :label="item.issueType" />
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

import type { SelectChangeEvent } from "primevue/select";
import type { AuditIssue } from "~/domain/audit-issue";

type SelectOption = { label: string, value: string }

const { issues } = defineProps({
  issues: {
    type: Array<AuditIssue>,
    required: true,
  },
});

// --- FILTERING ---
const selectedTools = ref<SelectOption[]>([])
const tools = computed<SelectOption[]>(() => {
    return [...new Set(issues.map((issue) => issue.tool))]
        .map(tool => {
            return { label: tool, value: tool }
        })
})

const selectedFileTypes = ref<SelectOption[]>([])
const fileTypes = computed<SelectOption[]>(() => {
    return [...new Set(issues.map((issue) => issue.fileType))]
        .map(fileType => {
            return { label: fileType || '<empty>', value: fileType }
        })
        .sort((a, b) => a.label.localeCompare(b.label))
})

const filterBySelectedTools = (issues: AuditIssue[]): AuditIssue[] => {
    if (selectedTools.value.length === 0) {
        return issues
    }
    return issues.filter((issue) => selectedTools.value.some(tool => tool.value === issue.tool))
}

const filterBySelectedFileTypes = (issues: AuditIssue[]): AuditIssue[] => {
    if (selectedFileTypes.value.length === 0) {
        return issues
    }
    return issues.filter((issue) => selectedFileTypes.value.some(fileType => fileType.value === issue.fileType))
}

const filteredIssues = computed<AuditIssue[]>(() => {
    return filterBySelectedFileTypes(filterBySelectedTools(issues))
})

// --- SORTING ---
const getFullPath = (issue: AuditIssue) => {
  return `${issue.location}/${issue.file}:${issue.line}`
}

const sortOptions = ref<SelectOption[]>([
    { label: 'Full Path', value: 'fullPath' },
    { label: 'Severity', value: 'severity'},
    { label: 'Effort', value: 'effort'},
]);
const sortSelectModel = ref<SelectOption | undefined>(sortOptions.value[0])
const sortOrder = ref<number>(1) // 1 = ascending, -1 = descending (only using ascending for now)
const sortFieldOrGetter = ref<((issue: AuditIssue) => string) | string>(getFullPath)

const onSortChange = (event: SelectChangeEvent) => {
    const selectedOption: SelectOption = event.value
    const selectedSortField: string = selectedOption.value

    if (selectedSortField === 'fullPath') {
        sortFieldOrGetter.value = getFullPath
    } else {
        sortFieldOrGetter.value = selectedSortField
    }
};
</script>
