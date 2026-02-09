<template>
    <div class="bg-slate-800/50 rounded-xl shadow-lg border-2 border-dashed border-slate-700 hover:border-cyan-500 transition-all p-6 flex flex-col items-center justify-center min-h-[400px] cursor-pointer group">
      <div class="p-4 bg-slate-700/50 rounded-full mb-4 group-hover:bg-cyan-500/20 transition-colors">
        <Icon name="lucide:plus" class="w-8 h-8 text-slate-500 group-hover:text-cyan-400 transition-colors" />
      </div>

      <h3 class="text-xl font-bold text-slate-400 group-hover:text-cyan-400 transition-colors mb-2">
        Add Tool
      </h3>

      <p class="text-sm text-slate-500 text-center mb-4">
        Integrate additional code analysis tools
      </p>

      <div class="space-y-2 text-center">
        <div class="text-xs text-slate-600">Suggestions:</div>
        <div class="flex flex-wrap gap-2 justify-center">
          <span class="px-2 py-1 bg-slate-700/30 text-slate-500 text-xs rounded">ESLint</span>
          <span class="px-2 py-1 bg-slate-700/30 text-slate-500 text-xs rounded">Psalm</span>
          <span class="px-2 py-1 bg-slate-700/30 text-slate-500 text-xs rounded">Rector</span>
          <span class="px-2 py-1 bg-slate-700/30 text-slate-500 text-xs rounded">OWASP Dependency-Check</span>
        </div>
      </div>
    </div>
</template>