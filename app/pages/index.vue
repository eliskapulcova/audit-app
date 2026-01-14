<template>
    <h1 class="text-2xl font-bold text-center m-5">Code Audit Report</h1>
    <Button v-if="!report" @click="downloadReport" class="m-5">Download report</Button>
    <div v-else>
        <AnalysisReport :report="report.analysisReport" />
        <IssueReport :issues="report.issueReport" />
    </div>
</template>

<script setup>
import AnalysisReport from '~/components/AnalysisReport.vue';
import IssueReport from '~/components/IssueReport.vue';
import { issues } from '~/mock/issues-report';
import analysisReport from '~/mock/2026-01-05-age-verification-analysis-report.json';

const report = ref(null)

function downloadReport() {
    report.value = {
        analysisReport,
        issueReport: issues
    }
}
</script>
