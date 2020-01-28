
const path = require('path')
const express = require('express')
const app = express()
const port = 11100

app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, '../client/index.html'))
})

app.get('/sux_client.js', (req, res) => {
  res.sendFile(path.join(__dirname, '../client/sux_client.js'))
})

app.get('/sux_dependencies.js', (req, res) => {
  res.sendFile(path.join(__dirname, '../client/sux_dependencies.js'))
})

app.listen(port, _ => console.log(`Listening on ${port}`))
