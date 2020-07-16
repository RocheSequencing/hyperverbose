# hyperverbose-core

Common data structures and APIs for all hyperverbose modules.

The most common use-case is to take database keys, such as Longs and UUIDs, and deterministically convert them to human-readable phonetic representations.
For example, Long `1234` might become `cherry-robot-table`.
Or with other config options and a targeted word list, UUID `12345678-9abc-def0-1234-56789abc` might become `BatmanCyclopsShiHellboy`.

## Warning!

Note that the string version will usually have _much_ lower bit entropy and depth than the source UUID or Long, due to the word list probably being relatively short compared to the bit length of a Long.
For this reason, you should **never** use the readable string version as a unique identifier — you're pretty much guaranteed to get duplicates / collisions.
Use the source Long / UUID as the unique identifier, and the string version as a human-readable label.

## Usage

Download and normalize a word list via [hyperverbose-words](../hyperverbose-words).

Drop your word list into your `resources` directory so it gets compiled into your JAR.
For now, let's assume it has a resource path like:

```java
String wordsResourcePath = "/hyperverbose/words.txt.gz";
```

Create a word list object:

```java
HyperverboseWords words = HyperverboseWordFile.wordsFromResource(wordsResourcePath);
```

There is also a version of that method which accepts a class in addition to the resource path.

Create an ID generator which uses that word list:

```java
HyperverboseIdGenerator generator = HyperverboseIdGenerator.builder()
    .words(words)
    .build();
```

If you're going to use the `randomId()` method you'll need to add a random number generator via the `random` builder.
There are a number of other interesting builder methods, such as `delimiter` and `wordCount`, but the minimum you need is just the word list.

You probably want to cache that generator, or at least cache the word list.

From then on, you can just use the `from*` methods:

```java
UUID storableId = UUID.randomUUID();
String readableId = generator.fromUuid(storableId);
```

The `HyperverbosePhraseGenerator` works the same way, but you can give it a sequence of lists which it will use in succession.
For example, you might have something like:

```java
HyperverboseWords colors = HyperverboseWordFile.wordsFromResource("/hyperverbose/colors.txt");
HyperverboseWords adjectives = HyperverboseWordFile.wordsFromResource("/hyperverbose/adj.txt");
HyperverboseWords nouns = HyperverboseWordFile.wordsFromResource("/hyperverbose/nouns.txt");
HyperverbosePhraseGenerator phrases = HyperverbosePhraseGenerator.builder()
    .wordCount(5)
    .extraStrategy(HyperverbosePhraseGenerator.ExtraStrategy.Extend)
    .wordList(nouns)
    .wordList(colors)
    .wordList(adjectives)
    .build();
```

Such a generator might return phrases like `bright-cheerful-oceanic-red-building`.
You'll note that the pattern there is `adjective-adjective-adjective-color-noun`.
This is because we used the `Extend` strategy, which repeats use of the last word list.
There's also a `Wrap` strategy, which does what it sounds like.

None of the generators will try to deduplicate words.
That is, it's entirely possible to get sequences like `ice-ice-baby` if the source value has uniform bits.
This is okay, and expected!