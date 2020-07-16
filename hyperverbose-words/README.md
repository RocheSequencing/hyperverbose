# hyperverbose-words

Utilities for dealing with word lists.
These tools are intended for offline use, or in CI/CD jobs.

## Usage

Show help:

```shell script
hyperverbose-words/words
```

You should see something like:

    Usage: wordlist [-hV] [COMMAND]
    Work with word list files
      -h, --help      Show this help message and exit.
      -V, --version   Print version information and exit.
    Commands:
      download    Download a word list
      list-types  Print a list of word list types available for use
      transform   Transform word list from source form to one usable by hyperverbose
      defaults    Show the default word list normalization parameters

### Download

You'll need a word list.

The `download` command has its own help:

```shell script
hyperverbose-words/words download --help
```

You should see something like:

    Usage: wordlist download [--gzip] [--overwrite] --dest-path=<destPath>
                             --source-format=<wordList>
    Download a word list
          --dest-path=<destPath>
                        Where to save the normalized intersection words file
          --gzip        Gzip output
          --overwrite   Overwrite the file if it already exists
          --source-format=<wordList>
                        Source word list format name

You can see the supported word lists via the following, where the `--with-*` arguments are optional:

```shell script
hyperverbose-words/words list-types --with-description
```

You should see something like:

    ALT12DICTS	A large word list compiled by combining 12 other lists
    JARGON    	Terms in the Jargon File
    OFFENSIVE 	Offensive words
    POS       	Parts of speech
    SWEAR     	Swear words

Example download:

```shell script
hyperverbose-words/words download \
  --source-format ALT12DICTS \
  --dest-path ~/Desktop/alt12dicts.txt \
  --overwrite
```

You should see something like:

    19:40:04.076 [main] INFO ...DownloadCommand - Downloaded: ~/Desktop/alt12dicts.txt

As you might expect, if you _do not_ specify `overwrite` and the file already exists, the downloader will exit without doing anything.
These word lists change infrequently, so you'll probably want to cache the results.

That file isn't quite ready for use.
You'll need to normalize it.

### Transform

Again, `transform` has its own help:

```shell script
hyperverbose-words/words transform --help
```

You should see something like:

    Usage: wordlist transform [--include-variants] [--pos-exclude-none]
                              [--pos-include-all] [--dict-count-min=<dictCountMin>]
                              [--length-max=<lengthMax>] [--length-min=<lengthMin>]
                              [--reject-pattern=<rejectPattern>]
                              --source-format=<wordList> --source-path=<sourcePath>
                              [--pos-exclude=<posExcludes>]...
                              [--pos-include=<posIncludes>]... [COMMAND]
    Transform word list from source form to one usable by hyperverbose
          --dict-count-min=<dictCountMin>
                               For aggregated word lists, how many dictionaries
                                 should the word appear in?
          --include-variants   For aggregated word lists, should we exclude variant
                                 spellings?
          --length-max=<lengthMax>
                               How many characters should the word be, at most?
          --length-min=<lengthMin>
                               How many characters should the word be, at least?
          --pos-exclude=<posExcludes>
                               Part of speech to be excluded
          --pos-exclude-none   Don't exclude any parts of speech
          --pos-include=<posIncludes>
                               Part of speech to be considered preferred
          --pos-include-all    Include all parts of speech (don't filter any)
          --reject-pattern=<rejectPattern>
                               Catch-all regular expression which will cause the
                                 word to be rejected if present
          --source-format=<wordList>
                               Word list source format name
          --source-path=<sourcePath>
                               Path to word list in source format
    Commands:
      intersect   Load a local file and keep only the words in both it and the
                    working set
      lowercase   Lowercase all words in the working list
      re-replace  Use a regular expression to replace parts of words in the working
                    list
      remove      Load a local file remove its words from the working set
      ucfirst     Uppercase the first letter of all words in the working list
      union       Load a local file and add its words to the working list
      uppercase   Uppercase all words in the working list
      write       Save the working set to a local file

The basic premise here is that you load a word list with the first set of `transform` arguments, then apply successive transformations in turn before using `write` to save out the result.
Most of the additional arguments, such as the length and part of speech, get applied when on file load, before applying any transforms.

You can see the defaults via:

```shell script
hyperverbose-words/words defaults
```

You should see something like:

    NormalizerConfiguration(dictCountMin=7, includeVariants=false, lengthMax=7, lengthMin=3, posExcludes=[Pronoun, Conjunction, ArticleDefinite, ArticleIndefinite, Interjection, Preposition], posIncludes=[Adjective, Noun], rejectPattern=[^a-zA-Z])

### Convert

At its most basic, you might want to normalize the file you downloaded earlier:

```shell script
hyperverbose-words/words transform \
  --source-format ALT12DICTS \
  --source-path ~/Desktop/alt12dicts.txt \
  --dest-path ~/Desktop/hyperverbose.txt.gz \
  --gzip \
  --overwrite
```

You should see something like:

    19:44:59.507 [main] DEBUG ...ANormalizer - Lines: 53789
    19:44:59.510 [main] DEBUG ...ANormalizer - Words added: 8246
    19:44:59.540 [main] INFO  ...NormalizeCommand - Normalized: ~/Desktop/hyperverbose.txt.gz

Note that the `gzip` flag is optional — it might be useful for some situations, but superfluous in others.
Also, again the `overwrite` flag is optional and probably undesirable for automation.

### More Complex Pipeline

A more realistic scenario might be to start with a big word list, narrow it down to specific parts of speech, and then eliminate potentially offensive words.
Presuming you have downloaded each of the word lists, you could do something like:

```shell script
hyperverbose-words/words                   \
  transform                                \
    --source-format ALT12DICTS             \
    --source-path ~/Desktop/alt12dicts.txt \
    --length-max 8                         \
  intersect                                \
    --source-format POS                    \
    --source-path ~/Desktop/pos.txt        \
    --length-max 8                         \
  remove                                   \
    --source-format SWEAR                  \
    --source-path ~/Desktop/swear.txt      \
    --length-max 99                        \
    --length-min 0                         \
  remove                                   \
    --source-format OFFENSIVE              \
    --source-path ~/Desktop/offensive.txt  \
    --length-max 99                        \
    --length-min 0                         \
  write                                    \
    --dest-path ~/Desktop/hyperverbose.txt \
    --overwrite
```

You should get debug output something like:

    17:25:54.575 [main] DEBUG ...TransformCommand - Loading: ALT12DICTS ~/Desktop/alt12dicts.txt
    17:25:54.710 [main] DEBUG ...ANormalizer - Lines: 53789
    17:25:54.710 [main] DEBUG ...ANormalizer - Words kept: 15369
    17:25:54.739 [main] DEBUG ...TransformCommand - Loading: POS ~/Desktop/pos.txt
    17:25:55.035 [main] DEBUG ...ANormalizer - Lines: 295172
    17:25:55.035 [main] DEBUG ...ANormalizer - Words kept: 79249
    17:25:55.159 [main] DEBUG ...TransformCommand - Words kept after intersect: 10638
    17:25:55.159 [main] DEBUG ...TransformCommand - Loading: SWEAR ~/Desktop/swear.txt
    17:25:55.160 [main] DEBUG ...ANormalizer - Lines: 77
    17:25:55.160 [main] DEBUG ...ANormalizer - Words kept: 76
    17:25:55.163 [main] DEBUG ...TransformCommand - Words kept after remove: 10617
    17:25:55.163 [main] DEBUG ...TransformCommand - Loading: OFFENSIVE ~/Desktop/offensive.txt
    17:25:55.166 [main] DEBUG ...ANormalizer - Lines: 1383
    17:25:55.166 [main] DEBUG ...ANormalizer - Words kept: 1358
    17:25:55.169 [main] DEBUG ...TransformCommand - Words kept after remove: 10376
    17:25:55.176 [main] DEBUG ...TransformCommand - Wrote: ~/Desktop/hyperverbose.txt

You could also split the files, such as putting nouns in one file and adjectives in another, by manipulating the `--pos-include` arguments.

Once you have prepared your word list or lists, you can use [hyperverbose-core](../hyperverbose-core) to generate identifiers and phrases with them.
