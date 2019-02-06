import com.eclipsesource.schema.drafts.Version7
import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import eu.timepit.refined._
import eu.timepit.refined.api._
import play.api.libs.json._


object SchemaRefined {
  trait Schemed
  trait Schema[A <: Schemed] {
    def schema: SchemaType
    def instance: A
  }

  implicit def validate[S <: Schemed](implicit schema: Schema[S]): Validate.Plain[JsValue, S] = {
    val validator = SchemaValidator(Some(Version7))

    Validate.fromPredicate(p => validator.validate(schema.schema, p).isSuccess, p => validator.validate(schema.schema, p).toString, schema.instance)
  }

  implicit def validateString[S <: Schemed](implicit schema: Schema[S]): Validate.Plain[String, S] = {
    val validator = SchemaValidator(Some(Version7))

    Validate.fromPredicate(p => validator.validate(schema.schema, Json.parse(p)).isSuccess, p => validator.validate(schema.schema, Json.parse(p)).toString, schema.instance)
  }

}

object MyApp extends App {
  import SchemaRefined._
  import Version7._

  val json =
    """
      |{
      |"$id": true,
      |"title": "aaa",
      |"body": "bbb"
      |}
    """.stripMargin

  final case class MySchemed() extends Schemed
  implicit val schema: Schema[MySchemed] = new Schema[MySchemed] {
    override def schema: SchemaType = Json.fromJson[SchemaType](Json.parse(
      """{
      |"properties": {
      |  "$id":    { "type": "integer" },
      |  "title": { "type": "string" },
      |  "body":  { "type": "string" }
      |}
    }""".stripMargin)).get
    override def instance: MySchemed = MySchemed()
  }

  val refinedValue: Either[String, Refined[JsValue, MySchemed]] = refineV[MySchemed](Json.parse(json))
  val refinedString: Either[String, Refined[String, MySchemed]] = refineV[MySchemed](json)

  println(refinedValue)
  println(refinedString)
}
