export function checkEmail(email) {
  if (
    !email.includes("@") ||
    !email.includes(".") ||
    !(email.indexOf(".") > email.indexOf("@"))
  ) {
    console.log("email bad");
    return "a valid email format";
  } else {
    return "";
  }
}

export function checkPassword(password) {
  if (
    !/\d/.test(password) ||
    !/[~`!@#$%^&*+=\-[\]\\';,/{}|\\":<>?]/g.test(password) ||
    !/[a-z]/.test(password) ||
    !(password.length > 7) ||
    !(password.length < 21)
  ) {
    console.log("password bad");
    return "a valid password";
  } else {
    return "";
  }
}
