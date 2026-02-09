<template>
  <div class="space-y-6" v-if="projectSummary">
    <HealthScore
      :health-score="healthScore"
      :project-summary="projectSummary"
    />
    <KPICards :kpis="kpis" />

    <div>
      <h2 class="text-2xl font-bold text-white mb-4">Tool Results</h2>
      <div class="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
        <SonarQubeCard :data="sonarQubeData" />
        <SemgrepCard :data="semgrepData" />
        <PHPCSCard :data="phpcsData" />
        <PHPStanCard :data="phpstanData" />
        <AddToolCard />
      </div>
    </div>

    <TrendChart :data="trendData" />
    <TopIssuesTable :issues="topIssues" />
    <RepositoryHealthMatrix :repositories="repositoryHealth" />

    <footer
      class="text-center text-sm text-slate-500 pt-8 border-t border-slate-800"
    >
      Quanti Code Quality Hub v1.0 — Data refreshed daily
    </footer>
  </div>
</template>

<script setup lang="ts">
import HealthScore from "~/components/v2/HealthScore.vue";
import KPICards from "~/components/v2/KPICards.vue";
import SonarQubeCard from "~/components/v2/audit-tools/SonarQubeCard.vue";
import SemgrepCard from "~/components/v2/audit-tools/SemgrepCard.vue";
import PHPCSCard from "~/components/v2/audit-tools/PHPCSCard.vue";
import PHPStanCard from "~/components/v2/audit-tools/PHPStanCard.vue";
import AddToolCard from "~/components/v2/AddToolCard.vue";
import TrendChart from "~/components/v2/TrendChart.vue";
import TopIssuesTable from "~/components/v2/TopIssuesTable.vue";
import RepositoryHealthMatrix from "~/components/v2/RepositoryHealthMatrix.vue";

import {
  healthScore,
  kpis,
  sonarQubeData,
  semgrepData,
  phpcsData,
  phpstanData,
  trendData,
  topIssues,
  repositoryHealth,
  projectSummaries,
} from "~/mock/v2/mock-data";

definePageMeta({
  layout: "v2",
});

const route = useRoute();
const projectSummary = computed(() => {
  return projectSummaries.find((p) => p.id === route.params.name);
});
</script>
