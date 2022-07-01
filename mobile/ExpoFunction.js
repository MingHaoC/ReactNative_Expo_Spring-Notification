import axios from "axios";

exports.registerForPushNotificationsAsync =
  async function registerForPushNotificationsAsync(Notifications) {
    let token;
    const { status: existingStatus } =
      await Notifications.getPermissionsAsync();
    let finalStatus = existingStatus;
    if (existingStatus !== "granted") {
      const { status } = await Notifications.requestPermissionsAsync();
      finalStatus = status;
    }
    if (finalStatus !== "granted") {
      alert("Failed to get push token for push notification!");
      return;
    }
    token = (await Notifications.getExpoPushTokenAsync()).data;

    console.log("token: " + token);

    return token;
  };

// Can use this function below, OR use Expo's Push Notification Tool-> https://expo.dev/notifications
exports.sendPushNotification = async function sendPushNotification(
  expoPushToken
) {
  const message = {
    to: expoPushToken,
    sound: "default",
    title: "Original Title",
    body: "And here is the body!",
    data: { someData: "goes here" },
  };

  const header = {
    Accept: "application/json",
    "Accept-encoding": "gzip, deflate",
    "Content-Type": "application/json",
  };

  try {
    const response = await axios.post(
      "https://exp.host/--/api/v2/push/send",
      message,
      header
    );
    console.log(JSON.stringify(response, null, 4));
  } catch (error) {
    console.log("error: " + error);
  }
};
