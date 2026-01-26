<template>
  <h1 class="text-3xl font-bold text-center m-5 mx-auto">Code Audit Report</h1>
  <Button
    v-if="!report"
    @click="USE_API ? downloadReportFromApi : downloadReport"
    class="m-5"
    >Download report</Button
  >
  <div v-else class="flex flex-col lg:flex-row">
    <AnalysisReport :report="report.analysisReport.data" />
    <IssueReport :issues="report.issueReport.auditIssueList" />
  </div>
</template>

<script setup>
import AnalysisReport from "~/components/AnalysisReport.vue";
import IssueReport from "~/components/IssueReport.vue";
import { issues } from "~/mock/issues-report";
import analysisReport from "~/mock/2026-01-05-age-verification-analysis-report.json";
import semgrepIssues from "~/mock/semgrep-results.json";

const config = useRuntimeConfig();
const API_URL = `${config.public.apiBase}/v1/report`;

const report = ref(null);

const USE_API = false;

function downloadReport() {
  report.value = {
    analysisReport: {
      data: analysisReport,
    },
    issueReport: {
      auditIssueList: semgrepIssues.results,
    },
  };
}

async function downloadReportFromApi() {
  const response = await $fetch(API_URL);
  report.value = response;
}
</script>
