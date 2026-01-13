<template>
    <div>
        <h3 class="text-xl font-bold mx-5 my-3">Run details</h3>
        <table class="mx-5 my-3 border-separate border-spacing-x-4">
            <tbody>
                <tr>
                    <td class="font-bold">Project</td>
                    <td class="px-2">{{ report.codeAnalysis.project }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Branch</td>
                    <td class="px-2">{{ report.codeAnalysis.branch }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Version</td>
                    <td class="px-2">{{ report.codeAnalysis.version }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Commit date</td>
                    <td class="px-2">{{ report.codeAnalysis.analyzedThe }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Date of analysis</td>
                    <td class="px-2">{{ report.codeAnalysis.date }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Analyzed by</td>
                    <td class="px-2">{{ report.codeAnalysis.by }}</td>
                </tr>
            </tbody>
        </table>
        <h3 class="text-xl font-bold mx-5 my-3">Analysis status</h3>
        <table class="mx-5 my-3 border-separate border-spacing-x-4">
            <tbody>
                <tr>
                    <td class="font-bold">Reliability</td>
                    <td>{{ report.synthesis.analysisStatus[0].Reliability }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Security</td>
                    <td>{{ report.synthesis.analysisStatus[0].Security }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Security Review</td>
                    <td>{{ report.synthesis.analysisStatus[0]['Security Review'] }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Maintainability</td>
                    <td>{{ report.synthesis.analysisStatus[0].Maintainability }}</td>
                </tr>
            </tbody>
        </table>
        <h3 class="text-xl font-bold mx-5 my-3">Metrics</h3>
        <table class="mx-5 my-3 border-separate border-spacing-x-4">
            <tbody>
                <tr>
                    <td class="font-bold">Coverage</td>
                    <td>{{ report.synthesis.metrics[0].Coverage }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Duplications</td>
                    <td>{{ report.synthesis.metrics[0].Duplications }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Comment density</td>
                    <td>{{ report.synthesis.metrics[0]['Comment density'] }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Median number of lines of code per file</td>
                    <td>{{ report.synthesis.metrics[0]['Median number of lines of code per file'] }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Adherence to coding standard</td>
                    <td>{{ report.synthesis.metrics[0]['Adherence to coding standard'] }}</td>
                </tr>
            </tbody>
        </table>
        <h3 class="text-xl font-bold mx-5 my-3">Technical Debt</h3>
        <table class="mx-5 my-3 border-separate border-spacing-x-4">
            <tbody>
                <tr>
                    <td class="font-bold">Reliability</td>
                    <td>{{ report.synthesis.detailedTechnicalDebt[0].Reliability }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Security</td>
                    <td>{{ report.synthesis.detailedTechnicalDebt[0].Security }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Maintainability</td>
                    <td>{{ report.synthesis.detailedTechnicalDebt[0].Maintainability }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Total</td>
                    <td>{{ report.synthesis.detailedTechnicalDebt[0].Total }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import report from '~/mock/2026-01-05-age-verification-analysis-report.json';
</script>
