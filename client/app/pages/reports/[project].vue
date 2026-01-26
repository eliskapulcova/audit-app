<template>
  <div class="project-report">
    <h1 class="text-3xl font-bold text-center m-5 mx-auto">
      {{ route.params.project }}
    </h1>
    <p v-if="pending">Loading...</p>
    <p v-else-if="error">Error: {{ error }}</p>
    <div v-else-if="data" class="flex flex-col max-w-4xl mx-auto">
      <!-- <AnalysisReport v-if="analysisReport" :report="analysisReport"/> -->
      <IssueReport :issues="allIssues" />
    </div>
  </div>
</template>

<script setup lang="ts">
const route = useRoute();

const { data, pending, error } = await useFetch(
  `/api/reports/${route.params.project}`,
);

const allIssues = computed(() => {
  return data.value?.flatMap((report) => report.issueList) ?? [];
});

const analysisReport = computed(() => {
  // TODO: design a general report interface
  return (
    data.value?.find((report) => report.analysisReport)?.analysisReport ?? null
  );
});
</script>
