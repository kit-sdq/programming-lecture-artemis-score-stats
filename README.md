# Artemis Score Stats

This script generates useful metrics about the performance of students in programming exercises using [Artemis](https://github.com/ls1intum/Artemis) and [programming-lecture-eclipse-artemis](https://github.com/kit-sdq/programming-lecture-eclipse-artemis).

The original motivation is to give teaching assistants (TA) detailed stats about how the students in their respective small learning groups are performing.

A for a small group might look like this (in german):

```

  Programmieren (WiSeX)

----------------------------------------------------------------------------------------------------

  Blatt X - Aufgabe X

  ZUSAMMENFASSUNG

  Teilnahmen                         ■■■■■□□□□□ 50%  16/32
  Mandatory bestanden                ■■■■■■■■□□ 75%  12/16
  Ø Punktzahl                        ■■■■■■□□□□ 59%  5.9/10.0
  Ø bestandene functional Tests      ■■■■■■■□□□ 71%  8.5/12.0
  Ø bestandene Modelling-Checks      ■■■■■■■□□□ 69%  3.4/5.0

  Ø manueller Abzug                  ■■■■■■■□□□ 67%  2.1/3.0

  HÄUFIG FEHLGESCHLAGENE FUNCTIONAL TESTS

  ■■■■■■■□□□ 69%  11/16              (FUNCTIONAL) Test Name 1
  ■■■□□□□□□□ 31%  5/16               (FUNCTIONAL) Test Name 2
  ■■■□□□□□□□ 25%  4/16               (FUNCTIONAL) Test Name 3
  ■■■□□□□□□□ 25%  4/16               (FUNCTIONAL) Test Name 4

  HÄUFIG FEHLGESCHLAGENE MODELLING-CHECKS

  ■■■■□□□□□□ 44%  7/16               Modeling-Check: Best Practices
  ■■■□□□□□□□ 31%  5/16               Modeling-Check: Comments
  ■■■□□□□□□□ 31%  5/16               Modeling-Check: Exception Handling
  ■■■□□□□□□□ 25%  4/16               Modeling-Check: Complexity
  ■■■□□□□□□□ 25%  4/16               Modeling-Check: Structure

  HÄUFIGE KORREKTUR ANMERKUNGEN (mind. eine Anmerkung pro Abgabe)

  ■■■■■■□□□□ 63%  10/16              Final [finalAttribute]
  ■■■■■■□□□□ 56%  9/16               unnötige Komplexität (klein) [unnecessaryComplex]
  ■■■■□□□□□□ 38%  6/16               Bedeutungslose Konstanten [meaninglessConstants]
  ■■■□□□□□□□ 31%  5/16               Schlechte Bezeichner [identifierNaming]
  ■■■□□□□□□□ 31%  5/16               Unbenutztes Element [unused]
  ■■■□□□□□□□ 31%  5/16               statisches Attribut [staticAttribute]
  ■■■□□□□□□□ 25%  4/16               Schwieriger Code [complexCode]
  ■■□□□□□□□□ 19%  3/16               Interface [interfaceAgainst]
  ■□□□□□□□□□ 13%  2/16               getterSetter für Listen [getterSetter]

  HÄUFIGE KORREKTUR ANMERKUNGEN (mind. eine Anmerkung pro Abgabe)

  ■■■■■■□□□□ 63%  10/16              Final [finalAttribute]
  ■■■■■■□□□□ 56%  9/16               unnötige Komplexität (klein) [unnecessaryComplex]
  ■■■■□□□□□□ 38%  6/16               Bedeutungslose Konstanten [meaninglessConstants]
  ■■■□□□□□□□ 31%  5/16               Schlechte Bezeichner [identifierNaming]
  ■■■□□□□□□□ 31%  5/16               Unbenutztes Element [unused]
  ■■■□□□□□□□ 31%  5/16               statisches Attribut [staticAttribute]
  ■■■□□□□□□□ 25%  4/16               Schwieriger Code [complexCode]
  ■■□□□□□□□□ 19%  3/16               Interface [interfaceAgainst]
  ■□□□□□□□□□ 13%  2/16               getterSetter für Listen [getterSetter]

  HÄUFIGE KORREKTUR ANMERKUNGEN (alle Anmerkungen)

  ■□□□□□□□□□ 13%  29/216             Final [finalAttribute]
  ■□□□□□□□□□ 10%  21/216             unnötige Komplexität (klein) [unnecessaryComplex]
  ■□□□□□□□□□ 8%   18/216             statisches Attribut [staticAttribute]
  ■□□□□□□□□□ 8%   18/216             Bedeutungslose Konstanten [meaninglessConstants]
  ■□□□□□□□□□ 7%   15/216             Kommentare [todo]
  ■□□□□□□□□□ 7%   15/216             Unbenutztes Element [unused]
  □□□□□□□□□□ 3%   7/216              Interface [interfaceAgainst]
  □□□□□□□□□□ 3%   6/216              Schwieriger Code [complexCode]
  □□□□□□□□□□ 3%   6/216              Schlechte Bezeichner [identifierNaming]

  INDIVIDUELLE KOMMENTARE

  -0.5P das ist ein individueller Kommentar
```

## Usage

```
Usage: <main class> [options]
  Options:
  * -host, -h
      Artemis host
  * -user, -u
      Artemis user name
  * -password, -p
      Artemis password
    -configs, -c
      The directory containing the grading config files. A config file must
      contain the short name of its corresponding exercise. For exercises
      without a config file all stats related to manual assessments will not
      be generated.
    -groups, -g
      The directory containing the group files. If no directory is specified,
      or if the directory is empty, only one report about all submissions will
      be generated.
    -output, -o
      The output directory.
      Default: .\stats
    -outputLimit, -l
      How many elements should at most be listed. This setting will be
      respected by all stats where the entirety of the result is not
      particularly significant.
```

> :warning: Just add `-p` to the command. The script will then prompt the user for the password without printing the password.

## Group files

Example:

```
# comment (line will be ignored)

# empty lines and lines only containing whitespace will also be ignored

# the student identifier have to be the same ones also used in artemis
u****
u****
```

A useful workflow could be to start with the original student assignment:

2.txt

```
u***a
u***b
u***c
u***d
```

After a few weeks, TAs may modify or add additional groups to get more detailed stats about the students who are actually attending the course.

2.v2.txt

```
u***a
u***b

# students who are not attending the course anymore
# u***c
# u***d

# students who switched to this course
# u***e
```
