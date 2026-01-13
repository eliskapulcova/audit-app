<template>
    <div>
        <table class="m-5">
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
                    <td class="font-bold">Date</td>
                    <td class="px-2">{{ report.codeAnalysis.date }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Analyzed</td>
                    <td class="px-2">{{ report.codeAnalysis.analyzedThe }}</td>
                </tr>
                <tr>
                    <td class="font-bold">By</td>
                    <td class="px-2">{{ report.codeAnalysis.by }}</td>
                </tr>
            </tbody>
        </table>
        <h2 class="text-xl font-bold ml-5">Analysis status</h2>
        <table class="m-5">
            <thead>
                <tr>
                    <th class="text-center px-2">Reliability</th>
                    <th class="text-center px-2">Security</th>
                    <th class="text-center px-2">Security Review</th>
                    <th class="text-center px-2">Maintainability</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td class="text-center">{{ report.synthesis.analysisStatus[0].Reliability }}</td>
                    <td class="text-center">{{ report.synthesis.analysisStatus[0].Security }}</td>
                    <td class="text-center">{{ report.synthesis.analysisStatus[0]['Security Review'] }}</td>
                    <td class="text-center">{{ report.synthesis.analysisStatus[0].Maintainability }}</td>
                </tr>
            </tbody>
        </table>
        <h2 class="text-xl font-bold ml-5">Metrics</h2>
        <table class="m-5">
            <!-- <thead>
                <tr>
                    <th class="text-center px-2">Coverage</th>
                    <th class="text-center px-2">Duplications</th>
                    <th class="text-center px-2">Comment density</th>
                    <th class="text-center px-2">Median number of lines of code per file</th>
                    <th class="text-center px-2">Adherence to coding standard</th>
                </tr>
            </thead> -->
            <tbody>
                <tr>
                    <td class="font-bold">Coverage</td>
                    <td class="ml-2">{{ report.synthesis.metrics[0].Coverage }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Duplications</td>
                    <td class="ml-2">{{ report.synthesis.metrics[0].Duplications }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Comment density</td>
                    <td class="ml-2">{{ report.synthesis.metrics[0]['Comment density'] }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Median number of lines of code per file</td>
                    <td class="ml-2">{{ report.synthesis.metrics[0]['Median number of lines of code per file'] }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Adherence to coding standard</td>
                    <td class="ml-2">{{ report.synthesis.metrics[0]['Adherence to coding standard'] }}</td>
                </tr>
            </tbody>
        </table>
        <h2 class="text-xl font-bold ml-5">Technical Debt</h2>
        <table class="m-5">
            <!-- <thead>
                <tr>
                    <th class="text-center px-2">Reliability</th>
                    <th class="text-center px-2">Security</th>
                    <th class="text-center px-2">Maintainability</th>
                    <th class="text-center px-2">Total</th>
                </tr>
            </thead> -->
            <tbody>
                <tr>
                    <td class="font-bold">Reliability</td>
                    <td class="ml-2">{{ report.synthesis.detailedTechnicalDebt[0].Reliability }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Security</td>
                    <td class="ml-2">{{ report.synthesis.detailedTechnicalDebt[0].Security }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Maintainability</td>
                    <td class="ml-2">{{ report.synthesis.detailedTechnicalDebt[0].Maintainability }}</td>
                </tr>
                <tr>
                    <td class="font-bold">Total</td>
                    <td class="ml-2">{{ report.synthesis.detailedTechnicalDebt[0].Total }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import report from '~/mock/2026-01-05-age-verification-analysis-report.json';
</script>
