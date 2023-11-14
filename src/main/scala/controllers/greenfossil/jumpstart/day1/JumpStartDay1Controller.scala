package controllers.greenfossil.jumpstart.day1

import com.greenfossil.data.mapping.Mapping
import com.greenfossil.data.mapping.Mapping.*
import com.greenfossil.thorium.{*, given}
import com.linecorp.armeria.server.annotation.{Get, Param, Post}

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object JumpStartDay1Controller:

  /*
   * Implement this method to returns:
   * - "Hi, {name}!" if name starts with a vowel
   * - "Hey, {name}!" if name starts with consonant
   * - "Hello stranger!" if name is empty
   */
  @Get("/greetMe")
  def greetMe(@Param name: String) = String:
    if (name.isBlank)
      s"Hello stranger!"
    else if (name.toLowerCase.startsWith("u") || name.toLowerCase.startsWith("e") || name.toLowerCase.startsWith("o") ||
      name.toLowerCase.startsWith("a") || name.toLowerCase.startsWith("i"))
      s"Hi, $name!"
    else
      s"Hey, $name!"

  /*
   * Implement this method to bind to the following fields:
   * 1. firstname : String, mandatory
   * 2. lastname: String, optional
   * 3. dob: java.time.LocalDate with format (dd/MM/yyyy)
   */
  private def signupMapping: Mapping[(String, Option[String], LocalDate)] = {
    tuple(
      "firstname" -> text,
      "lastname" -> optional(text),
      "dob" -> localDateUsing("dd/MM/yyyy")
    )
  }
  /*
   * Implement this method to bind the HTTP request's body to `signupMapping`.
   * If the data mapping has validation errors, return a BadRequest with text "Invalid Data".
   * If has no validation error, return an OK with text
   *    "Welcome {firstname} {lastname}! You were born on {dob<dd/MM/yyy>}."
   */
  @Post("/signup")
  def signup(using request: Request) =
    signupMapping.bindFromRequest().fold(
      errorForm =>
        val errorMessage = errorForm.errors.map:
          error =>
            s"Invalid Data"
        .mkString(". ")
        BadRequest(errorMessage),
      (firstname, lastname, dob) =>
        Ok(s"Welcome $firstname ${lastname.get}! You were born on ${dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}.")
    )

