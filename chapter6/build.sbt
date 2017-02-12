lazy val root = (project in file(".")).aggregate(remoting, echo, atleastonce)

lazy val remoting = project

lazy val echo = project

lazy val atleastonce = project
