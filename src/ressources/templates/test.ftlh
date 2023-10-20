<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Invoice</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            padding: 10px;
            text-align: center;
        }
        table {
            width: 60%;
            margin: 20px 0;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <h2>Invoice</h2>
    <p><b>Client</b> : BigCo</p>

    <table>
<tr>
            <th>Piece</th>
            <th>Seats sold</th>
            <th>Price</th>
        </tr>
        <#list performances as performance>
            <tr>
                <td>${performance.playName}</td>
                <td>${performance.audience}</td>
                <td>${performance.price?string.currency}</td>
            </tr>
        </#list>
        <tr>
            <td colspan="2"><b>Total owed:</b></td>
            <td><b>${totalAmount}</b></td>
        </tr>
        <tr>
            <td colspan="2"><b>Fidelity points earned:</b></td>
            <td><b>${fidelityPoints!}</b></td>
        </tr>
    </table>

    <p style="color:red;">Payment is required under 30 days. <b>We can break your knees if you don't do so.</b></p>
</body>
</html>
