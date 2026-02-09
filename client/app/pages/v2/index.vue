<template>
  <div class="space-y-6">
    <div
      class="bg-gradient-to-br from-slate-800 via-slate-800 to-slate-900 rounded-xl p-8 shadow-xl border border-slate-700"
    >
      <h1 class="text-3xl font-bold text-white mb-2">Projects Overview</h1>
      <p class="text-slate-400 mb-6">
        Comprehensive view of all projects and their code quality metrics
      </p>

      <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div class="bg-slate-900 p-4 rounded-lg">
          <div class="text-3xl font-bold text-cyan-400">
            {{ stats.totalProjects }}
          </div>
          <div class="text-sm text-slate-400">Total Projects</div>
        </div>
        <div class="bg-slate-900 p-4 rounded-lg">
          <div class="text-3xl font-bold text-blue-400">
            {{ stats.avgHealthScore }}
          </div>
          <div class="text-sm text-slate-400">Avg Health Score</div>
        </div>
        <div class="bg-slate-900 p-4 rounded-lg">
          <div class="text-3xl font-bold text-green-400">
            {{ stats.totalRepositories }}
          </div>
          <div class="text-sm text-slate-400">Total Repositories</div>
        </div>
        <div class="bg-slate-900 p-4 rounded-lg">
          <div class="text-3xl font-bold text-red-400">
            {{ stats.totalCriticalIssues }}
          </div>
          <div class="text-sm text-slate-400">Critical Issues</div>
        </div>
      </div>
    </div>

    <div class="bg-slate-800 rounded-xl p-6 shadow-lg border border-slate-700">
      <div
        class="flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between"
      >
        <div class="relative flex-1 w-full sm:max-w-md">
          <Icon
            name="lucide:search"
            class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400"
          />
          <input
            type="text"
            placeholder="Search projects..."
            v-model="searchQuery"
            class="w-full pl-10 pr-4 py-2 bg-slate-900 text-white rounded-lg border border-slate-700 focus:outline-none focus:ring-2 focus:ring-cyan-500"
          />
        </div>

        <div class="flex items-center gap-3">
          <Icon name="lucide:filter" class="w-5 h-5 text-slate-400" />
          <select
            v-model="sortBy"
            class="bg-slate-900 text-white px-4 py-2 rounded-lg border border-slate-700 focus:outline-none focus:ring-2 focus:ring-cyan-500"
          >
            <option value="name">Sort by Name</option>
            <option value="health">Sort by Health Score</option>
            <option value="issues">Sort by Critical Issues</option>
          </select>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <ProjectTile
        v-for="project in filteredProjects"
        :key="project.id"
        :project="project"
      />
    </div>

    <div
      v-if="filteredProjects.length === 0"
      class="text-center py-12 bg-slate-800 rounded-xl border border-slate-700"
    >
      <p class="text-slate-400">No projects found matching your search.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import ProjectTile from '~/components/v2/ProjectTile.vue'
import type { ProjectSummary } from '~/domain/types'

definePageMeta({
  layout: 'v2',
})

// TODO: Handle pending and error states
const { data, pending, error } = await useFetch<{
  projectSummaries: ProjectSummary[]
}>('/api/v2/project-summaries')
const projectSummaries = computed(() => data.value?.projectSummaries || [])

const searchQuery = ref<string>('')
const sortBy = ref<'name' | 'health' | 'issues'>('name')

const filteredProjects = computed(() => {
  return projectSummaries.value
    .filter(
      (project) =>
        project.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        project.description
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase())
    )
    .sort((a, b) => {
      switch (sortBy.value) {
        case 'health':
          return b.healthScore - a.healthScore
        case 'issues':
          return a.criticalIssues - b.criticalIssues
        case 'name':
        default:
          return a.name.localeCompare(b.name)
      }
    })
})

const stats = computed(() => {
  return {
    totalProjects: projectSummaries.value.length,
    avgHealthScore: Math.round(
      projectSummaries.value.reduce((sum, p) => sum + p.healthScore, 0) /
        projectSummaries.value.length
    ),
    totalRepositories: projectSummaries.value.reduce(
      (sum, p) => sum + p.repositoryCount,
      0
    ),
    totalCriticalIssues: projectSummaries.value.reduce(
      (sum, p) => sum + p.criticalIssues,
      0
    ),
  }
})
</script>
