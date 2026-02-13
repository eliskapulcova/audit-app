<template>
  <div class="bg-slate-800 rounded-xl p-6 shadow-lg border border-slate-700">
    <div class="mb-6">
      <h2 class="text-xl font-bold text-white mb-2">
        Repository Health Matrix
      </h2>
      <p class="text-sm text-slate-400">
        Quick overview of repository health across all analysis tools
      </p>
    </div>

    <div class="overflow-x-auto">
      <div class="inline-block min-w-full">
        <div class="flex gap-2">
          <div class="flex flex-col gap-2 pr-4">
            <div class="h-8" />
            <div
              v-for="repo in repositories"
              :key="repo.repository"
              class="h-10 flex items-center justify-end text-sm text-slate-300 font-medium"
            >
              {{ repo.repository }}
            </div>
          </div>

          <div class="flex-1">
            <div class="flex gap-2 mb-2">
              <div
                v-for="tool in tools"
                :key="tool"
                class="flex-1 h-8 flex items-center justify-center text-sm font-semibold text-slate-300"
              >
                {{ tool }}
              </div>
            </div>

            <div class="space-y-2">
              <div
                v-for="repo in repositories"
                :key="repo.repository"
                class="flex gap-2"
              >
                <div
                  v-for="tool in tools"
                  :key="tool"
                  :class="[
                    'flex-1 h-10 rounded transition-all cursor-pointer shadow-sm',
                    getHealthColor(getHealthStatus(repo, tool)),
                  ]"
                  :title="`${repo.repository} - ${tool}: ${getHealthStatus(repo, tool)}`"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div
      class="flex items-center justify-center gap-6 mt-6 pt-6 border-t border-slate-700"
    >
      <div class="flex items-center gap-2">
        <div class="w-4 h-4 rounded bg-green-500" />
        <span class="text-sm text-slate-400">Healthy</span>
      </div>
      <div class="flex items-center gap-2">
        <div class="w-4 h-4 rounded bg-yellow-500" />
        <span class="text-sm text-slate-400">Warning</span>
      </div>
      <div class="flex items-center gap-2">
        <div class="w-4 h-4 rounded bg-red-500" />
        <span class="text-sm text-slate-400">Critical</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { repositoryHealthStatusColors } from '~/config/visuals'
import type { HealthStatus, RepositoryHealth, Tool } from '../../domain/types'

defineProps<{
  repositories: RepositoryHealth[]
  tools: Tool[]
}>()

const getHealthColor = (health: HealthStatus) => {
  return repositoryHealthStatusColors[health]
}

const getHealthStatus = (repo: RepositoryHealth, tool: Tool): HealthStatus => {
  return repo.healthStatuses[tool]
}
</script>
