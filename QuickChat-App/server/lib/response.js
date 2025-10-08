export const responseStatusMsg = (res, statusCode, error, successInJson, messageInJson, customConsoleMsg) => {

  console.log(customConsoleMsg, error)

  res.status(statusCode).json({
    success: successInJson,
    message: messageInJson
  })
}