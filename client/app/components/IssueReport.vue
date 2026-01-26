<template>
  <div class="flex-1">
    <h2 class="text-2xl font-bold text-center m-5">Issue report</h2>
    <DataView
      :value="issues"
      responsive-layout="scroll"
      :sort-field="sortField"
      :sort-order="sortOrder"
      paginator
      :rows="10"
      :rows-per-page-options="[10, 20, 50]"
      class="mx-5 my-3"
    >
      <template #header>
        <span class="mr-2">Sort by:</span>
        <Select
          v-model="sortKey"
          :options="sortOptions"
          optionLabel="label"
          @change="onSortChange($event)"
        />
      </template>
      <template #list="slotProps">
        <div class="flex flex-col divide-y divide-gray-500">
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

const getFullPath = (issue: AuditIssue) => {
  return `${issue.location}/${issue.file}`
}

const sortOptions = ref([
    { label: 'Full Path', value: 'fullPath' },
    { label: 'Severity', value: 'severity'},
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
