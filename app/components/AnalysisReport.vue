<template>
    <div>
        <OverviewTable title="Run details" :data="runDetailsData" />
        <OverviewTable title="Analysis status" :data="analysisStatusData" />
        <OverviewTable title="Metrics" :data="metricsData" />
        <OverviewTable title="Technical debt" :data="technicalDebtData" />
    </div>
</template>

<script setup>
import OverviewTable from './auditOverview/OverviewTable.vue';

const { report } = defineProps({
    report: {
        type: Object,
        required: true
    }
})

const runDetailsData = computed(() => [
    {
        key: 'Project',
        value: report.codeAnalysis.project
    },
    {
        key: 'Branch',
        value: report.codeAnalysis.branch
    },
    {
        key: 'Version',
        value: report.codeAnalysis.version
    },
    {
        key: 'Commit date',
        value: report.codeAnalysis.analyzedThe
    },
    {
        key: 'Date of analysis',
        value: report.codeAnalysis.date
    },
    {
        key: 'Analyzed by',
        value: report.codeAnalysis.by
    }
])

const analysisStatusData = computed(() => [
    {
        key: 'Reliability',
        value: report.synthesis.analysisStatus[0].Reliability
    },
    {
        key: 'Security',
        value: report.synthesis.analysisStatus[0].Security
    },
    {
        key: 'Security Review',
        value: report.synthesis.analysisStatus[0]['Security Review']
    },
    {
        key: 'Maintainability',
        value: report.synthesis.analysisStatus[0].Maintainability
    }
])

const metricsData = computed(() => [
    {
        key: 'Coverage',
        value: report.synthesis.metrics[0].Coverage
    },
    {
        key: 'Duplications',
        value: report.synthesis.metrics[0].Duplications
    },
    {
        key: 'Comment density',
        value: report.synthesis.metrics[0]['Comment density']
    },
    {
        key: 'Median number of lines of code per file',
        value: report.synthesis.metrics[0]['Median number of lines of code per file']
    },
    {
        key: 'Adherence to coding standard',
        value: report.synthesis.metrics[0]['Adherence to coding standard']
    }
])

const technicalDebtData = computed(() => [
    {
        key: 'Reliability',
        value: report.synthesis.detailedTechnicalDebt[0].Reliability
    },
    {
        key: 'Security',
        value: report.synthesis.detailedTechnicalDebt[0].Security
    },
    {
        key: 'Maintainability',
        value: report.synthesis.detailedTechnicalDebt[0].Maintainability
    },
    {
        key: 'Total',
        value: report.synthesis.detailedTechnicalDebt[0].Total
    }
])
</script>
