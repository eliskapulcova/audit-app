<template>
  <ToolCard
    title="SonarQube"
    icon="lucide:database"
    :status="status"
    :last-run="data.lastRun"
  >
    <div class="grid grid-cols-3 gap-4">
      <div class="bg-slate-900 p-3 rounded-lg">
        <div class="text-2xl font-bold text-red-400">{{ data.bugs }}</div>
        <div class="text-xs text-slate-400">Bugs</div>
      </div>
      <div class="bg-slate-900 p-3 rounded-lg">
        <div class="text-2xl font-bold text-orange-400">
          {{ data.vulnerabilities }}
        </div>
        <div class="text-xs text-slate-400">Vulnerabilities</div>
      </div>
      <div class="bg-slate-900 p-3 rounded-lg">
        <div class="text-2xl font-bold text-yellow-400">
          {{ data.codeSmells }}
        </div>
        <div class="text-xs text-slate-400">Code Smells</div>
      </div>
    </div>

    <div class="grid grid-cols-2 gap-4">
      <div class="bg-slate-900 p-3 rounded-lg">
        <div class="text-xl font-bold text-green-400">{{ data.coverage }}%</div>
        <div class="text-xs text-slate-400">Coverage</div>
      </div>
      <div class="bg-slate-900 p-3 rounded-lg">
        <div class="text-xl font-bold text-blue-400">
          {{ data.duplications }}%
        </div>
        <div class="text-xs text-slate-400">Duplications</div>
      </div>
    </div>

    <div class="bg-slate-900 p-3 rounded-lg">
      <div class="flex items-center justify-between mb-2">
        <span class="text-sm text-slate-400">Quality Gate</span>
        <span
          :class="[
            'text-sm font-bold',
            data.qualityGate === 'Passed' ? 'text-green-400' : 'text-red-400',
          ]"
        >
          {{ data.qualityGate }}
        </span>
      </div>
      <div class="flex gap-4 text-sm">
        <div class="flex items-center gap-2">
          <div
            :class="[
              'w-6 h-6 rounded flex items-center justify-center text-xs font-bold text-white',
              getRatingColor(data.reliability),
            ]"
          >
            {{ data.reliability }}
          </div>
          <span class="text-slate-400">Reliability</span>
        </div>
        <div class="flex items-center gap-2">
          <div
            :class="[
              'w-6 h-6 rounded flex items-center justify-center text-xs font-bold text-white',
              getRatingColor(data.security),
            ]"
          >
            {{ data.security }}
          </div>
          <span class="text-slate-400">Security</span>
        </div>
        <div class="flex items-center gap-2">
          <div
            :class="[
              'w-6 h-6 rounded flex items-center justify-center text-xs font-bold text-white',
              getRatingColor(data.maintainability),
            ]"
          >
            {{ data.maintainability }}
          </div>
          <span class="text-slate-400">Maintainability</span>
        </div>
      </div>
    </div>

    <div class="bg-slate-900 p-3 rounded-lg">
      <div class="text-sm text-slate-400 mb-2">Issue Severity Breakdown</div>
      <div class="flex h-2 rounded-full overflow-hidden">
        <div
          class="bg-purple-600"
          :style="{
            width: `${(data.severityBreakdown.blocker / totalSeverity) * 100}%`,
          }"
          :title="`Blocker: ${data.severityBreakdown.blocker}`"
        />
        <div
          class="bg-red-600"
          :style="{
            width: `${(data.severityBreakdown.critical / totalSeverity) * 100}%`,
          }"
          :title="`Critical: ${data.severityBreakdown.critical}`"
        />
        <div
          class="bg-orange-500"
          :style="{
            width: `${(data.severityBreakdown.major / totalSeverity) * 100}%`,
          }"
          :title="`Major: ${data.severityBreakdown.major}`"
        />
        <div
          class="bg-yellow-500"
          :style="{
            width: `${(data.severityBreakdown.minor / totalSeverity) * 100}%`,
          }"
          :title="`Minor: ${data.severityBreakdown.minor}`"
        />
        <div
          class="bg-blue-500"
          :style="{
            width: `${(data.severityBreakdown.info / totalSeverity) * 100}%`,
          }"
          :title="`Info: ${data.severityBreakdown.info}`"
        />
      </div>
      <div class="flex justify-between mt-2 text-xs text-slate-500">
        <span>Blocker: {{ data.severityBreakdown.blocker }}</span>
        <span>Critical: {{ data.severityBreakdown.critical }}</span>
        <span>Major: {{ data.severityBreakdown.major }}</span>
      </div>
    </div>

    <div class="text-sm text-slate-500 text-center">
      Total: {{ totalIssues.toLocaleString() }} issues
    </div>
  </ToolCard>
</template>

<script setup lang="ts">
import { computed } from "vue";
import ToolCard from "../ToolCard.vue";
import type { SonarQubeData } from "../../../domain/types";

const props = defineProps<{
  data: SonarQubeData;
}>();

const status = computed(() => {
  if (props.data.qualityGate === "Failed" || props.data.vulnerabilities > 20)
    return "Critical";
  if (props.data.bugs > 100 || props.data.coverage < 70) return "Warning";
  return "Healthy";
});

const totalIssues = computed(
  () => props.data.bugs + props.data.vulnerabilities + props.data.codeSmells,
);

const totalSeverity = computed(
  () =>
    props.data.severityBreakdown.blocker +
    props.data.severityBreakdown.critical +
    props.data.severityBreakdown.major +
    props.data.severityBreakdown.minor +
    props.data.severityBreakdown.info,
);

const getRatingColor = (rating: string) => {
  switch (rating) {
    case "A":
      return "bg-green-500";
    case "B":
      return "bg-blue-500";
    case "C":
      return "bg-yellow-500";
    case "D":
      return "bg-orange-500";
    case "E":
      return "bg-red-500";
    default:
      return "bg-slate-500";
  }
};
</script>
