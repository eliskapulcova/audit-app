<template>
  <div class="bg-slate-800 rounded-xl p-6 shadow-lg border border-slate-700">
    <div
      class="flex flex-col sm:flex-row items-start sm:items-center justify-between mb-6 gap-4"
    >
      <h2 class="text-xl font-bold text-white">Top Issues</h2>

      <div class="flex flex-wrap gap-3">
        <select
          v-model="filterSeverity"
          class="bg-slate-700 text-white px-3 py-2 rounded-lg border border-slate-600 text-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
        >
          <option value="all">All Severities</option>
          <option
            v-for="severity in severities"
            :key="severity"
            :value="severity"
          >
            {{ severity }}
          </option>
        </select>

        <select
          v-model="filterTool"
          class="bg-slate-700 text-white px-3 py-2 rounded-lg border border-slate-600 text-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
        >
          <option value="all">All Tools</option>
          <option v-for="tool in tools" :key="tool" :value="tool">
            {{ tool }}
          </option>
        </select>
      </div>
    </div>

    <div class="overflow-x-auto">
      <table class="w-full">
        <thead>
          <tr class="border-b border-slate-700">
            <th
              class="text-left py-3 px-4 text-sm font-semibold text-slate-400 cursor-pointer hover:text-cyan-400"
              @click="handleSort('severity')"
            >
              <div class="flex items-center gap-2">
                Severity
                <Icon name="lucide:arrow-up-down" class="w-4 h-4" />
              </div>
            </th>
            <th
              class="text-left py-3 px-4 text-sm font-semibold text-slate-400 cursor-pointer hover:text-cyan-400"
              @click="handleSort('tool')"
            >
              <div class="flex items-center gap-2">
                Tool
                <Icon name="lucide:arrow-up-down" class="w-4 h-4" />
              </div>
            </th>
            <th
              class="text-left py-3 px-4 text-sm font-semibold text-slate-400"
            >
              Rule ID
            </th>
            <th
              class="text-left py-3 px-4 text-sm font-semibold text-slate-400"
            >
              Description
            </th>
            <th
              class="text-left py-3 px-4 text-sm font-semibold text-slate-400"
            >
              File Path
            </th>
            <th
              class="text-left py-3 px-4 text-sm font-semibold text-slate-400 cursor-pointer hover:text-cyan-400"
              @click="handleSort('firstDetected')"
            >
              <div class="flex items-center gap-2">
                First Detected
                <Icon name="lucide:arrow-up-down" class="w-4 h-4" />
              </div>
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="issue in sortedIssues"
            :key="issue.id"
            class="border-b border-slate-700/50 hover:bg-slate-700/30 transition-colors"
          >
            <td class="py-3 px-4">
              <span
                :class="[
                  'px-2 py-1 rounded text-xs font-semibold border',
                  getSeverityColor(issue.severity),
                ]"
              >
                {{ issue.severity }}
              </span>
            </td>
            <td class="py-3 px-4">
              <span
                :class="[
                  'px-2 py-1 rounded text-xs font-semibold',
                  getToolColor(issue.tool),
                ]"
              >
                {{ issue.tool }}
              </span>
            </td>
            <td class="py-3 px-4 text-sm text-cyan-400 font-mono">
              <a v-if="issue.ruleDocUrl" :href="issue.ruleDocUrl" target="_blank" class="cursor-pointer hover:underline">
                {{ issue.ruleId }}
              </a>
              <span v-else>
                {{ issue.ruleId }}
              </span>
            </td>
            <td class="py-3 px-4 text-sm text-slate-300 max-w-md truncate">
              {{ issue.description }}
            </td>
            <td
              class="py-3 px-4 text-xs text-slate-400 font-mono max-w-xs truncate"
            >
              <a v-if="issue.codeUrl" :href="issue.codeUrl" target="_blank" class="cursor-pointer hover:underline">
                {{ issue.filePath }}
              </a>
              <span v-else>
                {{ issue.filePath }}
              </span>
            </td>
            <td class="py-3 px-4 text-sm text-slate-400">
              {{ formatDate(issue.firstDetected) }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div
      v-if="sortedIssues.length === 0"
      class="text-center py-8 text-slate-500"
    >
      No issues found matching the selected filters.
    </div>

    <div class="mt-4 text-sm text-slate-500 text-center">
      Showing {{ sortedIssues.length }} of {{ issues.length }} issues
    </div>
  </div>
</template>

<script setup lang="ts">
import { issueSeverityColors, issueToolColors } from '~/config/visuals'
import {
  type Issue,
  type SeverityRating,
  type Tool,
  tools,
  severities,
} from '../domain/types'

const props = defineProps<{
  issues: Issue[]
}>()

const sortColumn = ref<keyof Issue>('severity')
const sortDirection = ref<'asc' | 'desc'>('asc')
const filterSeverity = ref<string>('all')
const filterTool = ref<string>('all')

const handleSort = (column: keyof Issue) => {
  if (sortColumn.value === column) {
    sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortColumn.value = column
    sortDirection.value = 'asc'
  }
}

const sortedIssues = computed(() => {
  const filtered = props.issues.filter((issue) => {
    if (
      filterSeverity.value !== 'all' &&
      issue.severity !== filterSeverity.value
    )
      return false
    if (filterTool.value !== 'all' && issue.tool !== filterTool.value)
      return false
    return true
  })

  return filtered.sort((a, b) => {
    const aValue = a[sortColumn.value]
    const bValue = b[sortColumn.value]

    if (sortColumn.value === 'severity') {
      const severityOrder = { Critical: 0, High: 1, Medium: 2, Low: 3 }
      const aOrder = severityOrder[a.severity as keyof typeof severityOrder]
      const bOrder = severityOrder[b.severity as keyof typeof severityOrder]
      return sortDirection.value === 'asc' ? aOrder - bOrder : bOrder - aOrder
    }

    if (typeof aValue === 'string' && typeof bValue === 'string') {
      return sortDirection.value === 'asc'
        ? aValue.localeCompare(bValue)
        : bValue.localeCompare(aValue)
    }

    return 0
  })
})

const getSeverityColor = (severity: SeverityRating) => {
  return issueSeverityColors[severity] || issueSeverityColors.default
}

const getToolColor = (tool: Tool) => {
  return issueToolColors[tool] || issueToolColors.default
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString()
}
</script>
