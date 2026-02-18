db.sonar_analyses.aggregate([
  { $unwind: "$issues" },
  { $group: {
      _id: {
        status: "$issues.status",
        type: "$issues.type",
        severity: "$issues.severity"
      },
      count: { $sum: 1 }
    }
  }
])
