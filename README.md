# hyperverbose

Hyperverbose is a collection of tools for working with word lists and grammatical constructs, then using them to generate human-repeatable identifiers.
For example, you might use [hyperverbose-core](./hyperverbose-core) to convert a Long (or UUID) to something readable:

    1234L  =>  pirate-ninja-battle

You also have control over the formatting.
Maybe you prefer `PirateNinjaBattle` instead.

Hyperverbose can help you download and generate word lists.
Maybe you want your repeatables to follow a specific pattern.
For example, Ubuntu uses `Adjective Animal` pairs.
Tools in [hyperverbose-words](./hyperverbose-words) can help you download lists of words, then filter and recombine them how you please.

# Usage

Check out [hyperverbose-words](./hyperverbose-words) for tools to download and generate word lists.

Check out [hyperverbose-core](./hyperverbose-core) for the programmatic SDK.

# Release

Hyperverbose artifacts are currently published through [GitHub packages](https://github.com/RocheSequencing/hyperverbose/packages).
In order to release artifacts you will need to set up your `settings.xml` file with the following repository and server configurations.

```
<settings xmlns="http://maven.apache.org/SETTINGS/1.1.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.1.0 http://maven.apache.org/xsd/settings-1.1.0.xsd">

	<activeProfiles>
		<activeProfile>github-RocheSequencing</activeProfile>
	</activeProfiles>

	<profiles>
		<profile>
			<id>github-RocheSequencing</id>
			<repositories>
				<repository>
					<id>central</id>
					<url>https://repo1.maven.org/maven2</url>
					<releases>
						<enabled>true</enabled>
					</releases>
				</repository>
				<repository>
					<id>github-RocheSequencing</id>
					<name>GitHub RocheSequencing Apache Maven Packages</name>
					<url>https://maven.pkg.github.com/RocheSequencing</url>
				</repository>
			</repositories>
		</profile>
	</profiles>

	<servers>
		<server>
			<id>github-RocheSequencing</id>
			<username>YOUR GH USERNAME</username>
			<password>YOUR GH TOKEN</password>
		</server>
	</servers>
</settings>
```

# Legal

Copyright 2020 Roche Sequencing, licensed under [the Apache License, Version 2.0](LICENSE).
Contributions will be accepted under the same terms as the [apache contributor agreements](https://www.apache.org/licenses/contributor-agreements.html).
