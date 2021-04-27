<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Security</title>
    <style>
        body {
            margin-left: 20%;
            margin-right: 20%;
        }
    </style>
    <h1>Upload test</h1>

    <form action="/upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file" size="50"/>
        <br/>
        <input type="submit" value="Upload File"/>
    </form>


</head>
<body>

</body>
</html>