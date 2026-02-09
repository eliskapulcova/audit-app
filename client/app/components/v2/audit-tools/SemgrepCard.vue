<template>
  <ToolCard
    title="Semgrep"
    icon="lucide:shield"
    :status="status"
    :last-run="data.lastRun"
  >
    <div class="bg-slate-900 p-4 rounded-lg">
      <div class="text-3xl font-bold text-white mb-1">
        {{ data.totalFindings }}
      </div>
      <div class="text-sm text-slate-400">Total Findings</div>
    </div>

    <div class="grid grid-cols-3 gap-3">
      <div class="bg-slate-900 p-3 rounded-lg border-l-4 border-red-500">
        <div class="text-xl font-bold text-red-400">{{ data.error }}</div>
        <div class="text-xs text-slate-400">Error</div>
      </div>
      <div class="bg-slate-900 p-3 rounded-lg border-l-4 border-yellow-500">
        <div class="text-xl font-bold text-yellow-400">{{ data.warning }}</div>
        <div class="text-xs text-slate-400">Warning</div>
      </div>
      <div class="bg-slate-900 p-3 rounded-lg border-l-4 border-blue-500">
        <div class="text-xl font-bold text-blue-400">{{ data.info }}</div>
        <div class="text-xs text-slate-400">Info</div>
      </div>
    </div>

    <div class="bg-slate-900 p-4 rounded-lg">
      <div class="text-sm text-slate-400 mb-3">Top Rule Categories</div>
      <div class="space-y-2">
        <div
          v-for="category in data.topCategories"
          :key="category.name"
          class="flex items-center justify-between"
        >
          <span class="text-sm text-slate-300 font-mono">{{
            category.name
          }}</span>
          <span class="text-sm font-bold text-cyan-400">{{
            category.count
          }}</span>
        </div>
      </div>
    </div>

    <div class="bg-slate-900 p-4 rounded-lg">
      <div class="text-sm text-slate-400 mb-3">
        Findings Trend (Last 8 Scans)
      </div>
      <div class="w-full h-[120px]">
        <Chart
          type="line"
          :data="lineChartData"
          :options="lineChartOptions"
          class="w-full h-full"
        />
      </div>
    </div>

    <div class="bg-slate-900 p-4 rounded-lg">
      <div class="flex items-center justify-between">
        <span class="text-sm text-slate-400">OWASP Top 10 Coverage</span>
        <span class="text-xl font-bold text-green-400"
          >{{ data.owaspCoverage }}%</span
        >
      </div>
      <div class="mt-2 h-2 bg-slate-700 rounded-full overflow-hidden">
        <div
          class="h-full bg-gradient-to-r from-green-500 to-cyan-500"
          :style="{ width: `${data.owaspCoverage}%` }"
        />
      </div>
    </div>
  </ToolCard>
</template>

<script setup lang="ts">
import { computed } from "vue";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
} from "chart.js";
import ToolCard from "../ToolCard.vue";
import type { SemgrepData } from "../../../domain/types";

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
);

const props = defineProps<{
  data: SemgrepData;
}>();

const status = computed(() => {
  if (props.data.error > 15) return "Critical";
  if (props.data.warning > 80) return "Warning";
  return "Healthy";
});

const lineChartData = computed(() => {
  return {
    labels: props.data.trendData.map((_, i) => i + 1),
    datasets: [
      {
        data: props.data.trendData,
        borderColor: "#06b6d4",
        borderWidth: 2,
        pointBackgroundColor: "#06b6d4",
        pointRadius: 3,
        tension: 0.4,
      },
    ],
  };
});

const lineChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: "#1e293b",
      titleColor: "#94a3b8",
      bodyColor: "#f8fafc",
      padding: 10,
      displayColors: false,
    },
  },
  scales: {
    x: {
      grid: { display: false },
      ticks: { color: "#64748b" },
    },
    y: {
      grid: { color: "#334155" },
      ticks: { color: "#64748b" },
    },
  },
};
</script>
