package io.mvillafuertem.mdoc.modifier.plantuml

import io.mvillafuertem.mdoc.modifier.plantuml.PlantUMLModifier.PlantUMLError
import net.sourceforge.plantuml.FileFormat
import zio.test.Assertion.{equalTo, fails}
import zio.test._
import zio.test.environment.TestEnvironment

object PlantUMLModifierSpec extends DefaultRunnableSpec {

  override def spec: ZSpec[TestEnvironment, Any] =
    suite(getClass.getSimpleName)(
      testM("fails when the modifier does not match the correct format")(
        assertM(
          PlantUMLModifier.buildFromString("onlyPathName").run
        )(fails(equalTo(PlantUMLError("[ onlyPathName ] modifier doesn't match with the correct format [ file/path/name:fileFormat ]"))))
      ),
      testM("fails when the file format is invalid")(
        assertM(
          PlantUMLModifier.selectFileFormat("invalidFormat").run
        )(fails(equalTo(PlantUMLError("[ invalidFormat ] file format is invalid, only supports [PNG, ATXT]"))))
      ),
      testM("fails when the file format is not support")(
        assertM(
          PlantUMLModifier.selectFileFormat(FileFormat.EPS.toString).run
        )(fails(equalTo(PlantUMLError("[ EPS ] file format is invalid, only supports [PNG, ATXT]"))))
      )
    )

}
