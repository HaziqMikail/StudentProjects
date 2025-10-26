<?php
require_once 'config.php';
$conn = getDBConnection();

// Get all available months
$monthsQuery = "SELECT DISTINCT DATE_FORMAT(created_at, '%Y-%m') as month_key,
                DATE_FORMAT(created_at, '%M %Y') as month_label
                FROM expenses
                ORDER BY month_key DESC";
$monthsResult = $conn->query($monthsQuery);
$months = [];
while ($row = $monthsResult->fetch_assoc()) {
    $months[$row['month_key']] = $row['month_label'];
}

// Get selected month or default to latest
$selectedMonth = $_GET['month'] ?? (array_key_first($months) ?? date('Y-m'));

// Get expenses for selected month
$dataByDay = [];
$monthTotal = 0;

if ($selectedMonth) {
    $stmt = $conn->prepare("SELECT category, amount, created_at 
                           FROM expenses 
                           WHERE DATE_FORMAT(created_at, '%Y-%m') = ?
                           ORDER BY created_at DESC");
    $stmt->bind_param("s", $selectedMonth);
    $stmt->execute();
    $result = $stmt->get_result();
    
    while ($row = $result->fetch_assoc()) {
        $dayKey = date('Y-m-d', strtotime($row['created_at']));
        $dayLabel = date('j F Y', strtotime($row['created_at']));
        
        $dataByDay[$dayKey]['label'] = $dayLabel;
        $dataByDay[$dayKey]['entries'][] = [
            'category' => $row['category'],
            'amount' => $row['amount'],
            'time' => date('g:i A', strtotime($row['created_at']))
        ];
        $monthTotal += $row['amount'];
    }
    
    $stmt->close();
}

$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Expense History by Month</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f7f8;
      padding: 40px 20px;
      max-width: 900px;
      margin: auto;
    }

    h1 {
      text-align: center;
      color: #333;
      margin-bottom: 40px;
    }

    .filter-section {
      text-align: center;
      margin-bottom: 30px;
      background: white;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.05);
    }

    select {
      padding: 8px 12px;
      font-size: 16px;
      border-radius: 6px;
      border: 1px solid #ccc;
      min-width: 200px;
    }

    .month-summary {
      background: #e7f3ff;
      padding: 15px;
      border-radius: 8px;
      margin-bottom: 30px;
      text-align: center;
    }

    .month-summary h2 {
      margin: 0 0 10px 0;
      color: #007bff;
    }

    .month-summary .total {
      font-size: 32px;
      font-weight: bold;
      color: #333;
    }

    h3 {
      margin-top: 30px;
      color: #444;
      border-left: 4px solid #007bff;
      padding-left: 10px;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
      background: #fff;
      box-shadow: 0 2px 6px rgba(0,0,0,0.05);
      border-radius: 8px;
      overflow: hidden;
    }

    th, td {
      padding: 12px;
      text-align: left;
    }

    th {
      background-color: #007bff;
      color: white;
      font-weight: 600;
    }

    tr:nth-child(even) {
      background-color: #f9f9f9;
    }

    .amount-col {
      text-align: right;
      font-weight: 600;
      white-space: nowrap;
    }

    .time-col {
      color: #666;
      font-size: 14px;
      width: 100px;
    }

    .day-total {
      text-align: right;
      font-weight: bold;
      padding: 12px;
      color: #333;
      background-color: #e9ecef;
      border-top: 2px solid #dee2e6;
    }

    .no-data {
      text-align: center;
      padding: 40px;
      color: #666;
      background: white;
      border-radius: 8px;
    }

    .back-button {
      display: block;
      margin: 50px auto 0;
      background-color: #007bff;
      color: #fff;
      padding: 12px 24px;
      border: none;
      border-radius: 8px;
      font-size: 16px;
      cursor: pointer;
      text-align: center;
      text-decoration: none;
      transition: background-color 0.3s;
      width: fit-content;
    }

    .back-button:hover {
      background-color: #0056b3;
    }
  </style>
</head>
<body>

<h1>Monthly Expense History</h1>

<div class="filter-section">
  <form method="GET">
    <label><strong>Select Month:</strong></label>
    <select name="month" onchange="this.form.submit()">
      <option value="">-- Choose Month --</option>
      <?php foreach ($months as $key => $label): ?>
        <option value="<?= $key ?>" <?= ($key === $selectedMonth) ? 'selected' : '' ?>>
          <?= $label ?>
        </option>
      <?php endforeach; ?>
    </select>
  </form>
</div>

<?php if ($selectedMonth && !empty($dataByDay)): ?>
  <div class="month-summary">
    <h2><?= $months[$selectedMonth] ?? date('F Y', strtotime($selectedMonth)) ?></h2>
    <div class="total">RM <?= number_format($monthTotal, 2) ?></div>
    <p style="margin: 5px 0 0 0; color: #666;">Total Expenses</p>
  </div>

  <?php foreach ($dataByDay as $dayKey => $dayData): ?>
    <h3><?= $dayData['label'] ?></h3>
    <table>
      <tr>
        <th>Category</th>
        <th class="time-col">Time</th>
        <th class="amount-col">Amount (RM)</th>
      </tr>
      <?php 
      $dayTotal = 0;
      foreach ($dayData['entries'] as $entry): 
        $dayTotal += $entry['amount'];
      ?>
        <tr>
          <td><?= htmlspecialchars($entry['category']) ?></td>
          <td class="time-col"><?= $entry['time'] ?></td>
          <td class="amount-col">RM <?= number_format($entry['amount'], 2) ?></td>
        </tr>
      <?php endforeach; ?>
      <tr>
        <td colspan="3" class="day-total">
          Day Total: RM <?= number_format($dayTotal, 2) ?>
        </td>
      </tr>
    </table>
  <?php endforeach; ?>

<?php else: ?>
  <div class="no-data">
    <h3>No expenses recorded for this month</h3>
    <p>Start tracking your expenses to see them here!</p>
  </div>
<?php endif; ?>

<a class="back-button" href="main.php">← Back to Add Expense</a>

</body>
</html>