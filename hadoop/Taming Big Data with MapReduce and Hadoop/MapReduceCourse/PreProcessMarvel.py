from collections import defaultdict

comicsByChar = defaultdict(list)
charsByComics = defaultdict(list)
with open("Marvel-names.txt", 'w') as out:
    with open("Marvel.txt") as f:
        lastHero = ''
        connections = []
        for line in f:
            #Skip lines that aren't graph edges
            if not '"' in line:
                if not line[0] == '*':
                    fields = line.split()
                    heroID = fields[0]
                    numComics = len(fields) - 1
                    comics = fields[-numComics:]
                    for comic in comics:
                        comicsByChar[heroID].append(comic)
                        charsByComics[comic].append(heroID)
            else:
                out.write(line)

        f.close()
    out.close()

with open("Marvel-graph.txt", 'w') as out:
    for character in comicsByChar.keys():
        out.write(character)
        out.write(' ')
        connectionList = {}
        for comic in comicsByChar[character]:
            for connection in charsByComics[comic]:
                    connectionList[connection] = 1
        keyCount = 0
        for key in connectionList.keys():
            if (key != character):
                out.write(key)
                out.write(' ')
                keyCount = keyCount + 1
                if (keyCount > 500):
                    keyCount = 0
                    out.write("\n" + character + " ")
        out.write("\n")

out.close()
