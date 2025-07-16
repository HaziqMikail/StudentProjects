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

    form {
      text-align: center;
      margin-bottom: 30px;
    }

    select {
      padding: 8px 12px;
      font-size: 16px;
      border-radius: 6px;
      border: 1px solid #ccc;
    }

    h2 {
      margin-top: 40px;
      color: #007bff;
      border-bottom: 2px solid #007bff;
      padding-bottom: 5px;
    }

    h3 {
      margin-top: 30px;
      color: #444;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
      background: #fff;
      box-shadow: 0 2px 6px rgba(0,0,0,0.05);
    }

    th, td {
      padding: 12px;
      text-align: left;
      border: none;
    }

    th.amount-col, td.amount-col {
      width: 120px;
      text-align: left;
      white-space: nowrap;
    }

    th {
      background-color: #007bff;
      color: white;
    }

    tr:nth-child(even) {
      background-color: #f9f9f9;
    }

    .day-total {
      text-align: right;
      font-weight: bold;
      padding: 12px;
      color: #333;
      background-color: #e9ecef;
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

<h1>📊 Monthly Expense History</h1>

<?php
$lines = file_exists("expenses.txt") ? file("expenses.txt", FILE_IGNORE_NEW_LINES) : [];
$monthsFound = [];
$dataByMonth = [];

// Group entries by month and day
foreach ($lines as $line) {
    list($datetime, $item, $amount) = explode(" | ", $line);
    $timestamp = strtotime($datetime);
    $monthKey = date("Y-m", $timestamp);
    $monthName = date("F Y", $timestamp);
    $dayKey = date("Y-m-d", $timestamp);
    $dayLabel = date("j F", $timestamp);

    $monthsFound[$monthKey] = $monthName;
    $dataByMonth[$monthKey][$dayKey]['label'] = $dayLabel;
    $dataByMonth[$monthKey][$dayKey]['entries'][] = [
        'item' => $item,
        'amount' => $amount
    ];
}

// Sort months descending
$sortedMonths = array_keys($monthsFound);
rsort($sortedMonths);
$selectedMonth = $_GET['month'] ?? ($sortedMonths[0] ?? '');

// Dropdown menu
echo "<form method='GET'>
        <label>Select Month:</label>
        <select name='month' onchange='this.form.submit()'>
        <option value=''>-- Choose Month --</option>";

$monthKeys = array_keys($monthsFound);
sort($monthKeys);
$firstMonth = $monthKeys[0] ?? date("Y-m");
$lastMonth = $monthKeys[count($monthKeys) - 1] ?? date("Y-m");

$start = strtotime(date("Y", strtotime($firstMonth)) . "-01-01");
$end = strtotime($lastMonth . "-01");

while ($start <= $end) {
    $key = date("Y-m", $start);
    $label = date("F Y", $start);
    $selected = ($key === $selectedMonth) ? 'selected' : '';
    echo "<option value='$key' $selected>$label</option>";
    $start = strtotime("+1 month", $start);
}

echo "</select></form>";

// Display results
if ($selectedMonth && isset($dataByMonth[$selectedMonth])) {
    $monthLabel = $monthsFound[$selectedMonth];
    echo "<h2>Expenses for $monthLabel</h2>";
    $days = $dataByMonth[$selectedMonth];
    krsort($days);

    foreach ($days as $dayKey => $dayData) {
        echo "<h3>{$dayData['label']}</h3>";
        echo "<table>
                <tr>
                    <th>Item</th>
                    <th class='amount-col'>Amount (RM)</th>
                </tr>";

        $dayTotal = 0;
        foreach ($dayData['entries'] as $entry) {
            $amount = floatval(str_replace("RM", "", $entry['amount']));
            $dayTotal += $amount;

            echo "<tr>
                    <td>{$entry['item']}</td>
                    <td class='amount-col'>RM" . number_format($amount, 2) . "</td>
                  </tr>";
        }

        echo "<tr>
                <td colspan='2' class='day-total'>Total: RM" . number_format($dayTotal, 2) . "</td>
              </tr>";
        echo "</table>";
    }
} else {
    echo "<p>No expenses recorded for this month.</p>";
}
?>

<a class="back-button" href="main.html">← Back to Add Expense</a>

</body>
</html>
