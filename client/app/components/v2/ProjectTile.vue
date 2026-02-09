<template>
  <NuxtLink
    :to="`/v2/project/${project.id}`"
    class="block bg-slate-800 rounded-xl p-6 shadow-lg border border-slate-700 hover:border-cyan-500 transition-all hover:shadow-cyan-500/10 hover:scale-[1.02] group"
  >
    <div class="flex items-start justify-between mb-4">
      <div class="flex items-center gap-3">
        <div class="p-3 bg-gradient-to-br from-cyan-500 to-blue-500 rounded-lg group-hover:scale-110 transition-transform">
          <Icon name="lucide:folder-git-2" class="w-6 h-6 text-white" />
        </div>
        <div>
          <h3 class="text-xl font-bold text-white group-hover:text-cyan-400 transition-colors">
            {{ project.name }}
          </h3>
          <p class="text-sm text-slate-400">{{ project.repositoryCount }} repositories</p>
        </div>
      </div>

      <div :class="['px-3 py-1 rounded-full flex items-center gap-2', status.color]">
        <Icon :name="status.icon" class="w-4 h-4" />
        <span class="text-sm font-semibold">{{ status.label }}</span>
      </div>
    </div>

    <p class="text-sm text-slate-400 mb-4 line-clamp-2">{{ project.description }}</p>

    <div class="grid grid-cols-2 gap-3 mb-4">
      <div class="bg-slate-900 p-3 rounded-lg">
        <div class="flex items-center gap-2 mb-1">
          <div :class="['text-2xl font-bold bg-gradient-to-r bg-clip-text text-transparent', getGradeColor(project.grade)]">
            {{ project.healthScore }}
          </div>
          <div class="text-xs text-slate-500">/ 100</div>
        </div>
        <div class="text-xs text-slate-400">Health Score</div>
        <div :class="['inline-flex px-2 py-0.5 rounded text-xs font-bold mt-1 bg-gradient-to-r text-white', getGradeColor(project.grade)]">
          Grade {{ project.grade }}
        </div>
      </div>

      <div class="bg-slate-900 p-3 rounded-lg">
        <div class="flex items-center gap-2 mb-1">
          <Icon name="lucide:shield" class="w-4 h-4 text-green-400" />
          <div class="text-2xl font-bold text-green-400">{{ project.coverage }}%</div>
        </div>
        <div class="text-xs text-slate-400">Code Coverage</div>
      </div>
    </div>

    <div class="grid grid-cols-2 gap-3 mb-4">
      <div class="bg-slate-900 p-3 rounded-lg">
        <div class="text-xl font-bold text-orange-400">{{ project.totalIssues.toLocaleString() }}</div>
        <div class="text-xs text-slate-400">Total Issues</div>
      </div>

      <div class="bg-slate-900 p-3 rounded-lg">
        <div class="text-xl font-bold text-red-400">{{ project.criticalIssues }}</div>
        <div class="text-xs text-slate-400">Critical Issues</div>
      </div>
    </div>

    <div class="flex items-center justify-between pt-4 border-t border-slate-700">
      <div class="flex items-center gap-2 text-sm text-slate-400">
        <span>Last scan:</span>
        <span class="text-cyan-400 font-medium">
          {{ formatDate(project.lastScan) }}
        </span>
      </div>

      <div :class="['flex items-center gap-1 text-sm', project.trend > 0 ? 'text-green-400' : 'text-red-400']">
        <Icon
          v-if="project.trend > 0"
          name="lucide:trending-up"
          class="w-4 h-4"
        />
        <Icon
          v-else
          name="lucide:trending-down"
          class="w-4 h-4"
        />
        <span class="font-semibold">{{ Math.abs(project.trend) }}%</span>
      </div>
    </div>
  </NuxtLink>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { ProjectSummary } from '../../domain/types';

const props = defineProps<{
  project: ProjectSummary;
}>();

const getGradeColor = (grade: string) => {
  switch (grade) {
    case 'A':
      return 'from-green-500 to-emerald-600';
    case 'B':
      return 'from-blue-500 to-cyan-600';
    case 'C':
      return 'from-yellow-500 to-orange-500';
    case 'D':
      return 'from-orange-500 to-red-500';
    case 'F':
      return 'from-red-500 to-rose-700';
    default:
      return 'from-slate-500 to-slate-600';
  }
};

const status = computed(() => {
  if (props.project.criticalIssues > 15) {
    return { label: 'Critical', color: 'text-red-400 bg-red-500/10', icon: 'lucide:alert-triangle' };
  }
  if (props.project.criticalIssues > 8) {
    return { label: 'Warning', color: 'text-yellow-400 bg-yellow-500/10', icon: 'lucide:alert-triangle' };
  }
  return { label: 'Healthy', color: 'text-green-400 bg-green-500/10', icon: 'lucide:check-circle-2' };
});

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
};
</script>
