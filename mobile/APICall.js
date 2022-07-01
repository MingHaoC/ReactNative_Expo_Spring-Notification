import axios from "axios";

const header = {
  Accept: "application/json",
  "Content-Type": "application/json",
};

exports.registerUser = async () => {
  const user = {
    address: "123 some address, windsor ontario Canada H0H 0H0",
    phoneNumber: "123-456-7890",
    email: "test4@uwindsor.ca",
    password: "password123",
    firstName: "test",
    lastName: "user",
  };

  try {
    const response = await axios.post(
      "http://172.17.32.1:8080/user/register",
      user,
      header
    );
    if (response.status === "200") console.log("Successfully added user");
    else console.log(response);
  } catch (error) {
    console.log("error: ", error);
  }
};

exports.registerExpoToken = async (
  email = "test@uwindsor.ca",
  expoPushToken
) => {
  const user = {
    email: email,
    expoToken: expoPushToken,
  };

  try {
    const result = await axios.post(
      `http://172.17.32.1:8080/expoToken/register`,
      user,
      header
    );
    if ((result.status = "200"))
      console.log("Expo token has successfully been stored");
    else
      console.log(
        "Error: something has gone wrong trying to save expo notification token"
      );
  } catch (error) {
    console.log("Error: " + error);
  }
};
