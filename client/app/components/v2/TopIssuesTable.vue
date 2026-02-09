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
          <option value="Critical">Critical</option>
          <option value="High">High</option>
          <option value="Medium">Medium</option>
          <option value="Low">Low</option>
        </select>

        <select
          v-model="filterTool"
          class="bg-slate-700 text-white px-3 py-2 rounded-lg border border-slate-600 text-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
        >
          <option value="all">All Tools</option>
          <option value="SonarQube">SonarQube</option>
          <option value="Semgrep">Semgrep</option>
          <option value="PHPCS">PHPCS</option>
          <option value="PHPStan">PHPStan</option>
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
              {{ issue.ruleId }}
            </td>
            <td class="py-3 px-4 text-sm text-slate-300 max-w-md truncate">
              {{ issue.description }}
            </td>
            <td
              class="py-3 px-4 text-xs text-slate-400 font-mono max-w-xs truncate"
            >
              {{ issue.filePath }}
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
import { ref, computed } from 'vue'
import type { Issue } from '../../domain/types'

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

const getSeverityColor = (severity: string) => {
  switch (severity) {
    case 'Critical':
      return 'bg-red-500/10 text-red-400 border-red-500'
    case 'High':
      return 'bg-orange-500/10 text-orange-400 border-orange-500'
    case 'Medium':
      return 'bg-yellow-500/10 text-yellow-400 border-yellow-500'
    case 'Low':
      return 'bg-blue-500/10 text-blue-400 border-blue-500'
    default:
      return 'bg-slate-500/10 text-slate-400 border-slate-500'
  }
}

const getToolColor = (tool: string) => {
  switch (tool) {
    case 'SonarQube':
      return 'bg-cyan-500/10 text-cyan-400'
    case 'Semgrep':
      return 'bg-green-500/10 text-green-400'
    case 'PHPCS':
      return 'bg-yellow-500/10 text-yellow-400'
    case 'PHPStan':
      return 'bg-red-500/10 text-red-400'
    default:
      return 'bg-slate-500/10 text-slate-400'
  }
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString()
}
</script>
