Тестовое задание ШИФТ ЦФТ

# Требования
* java 11
* gradle 7.4

# Инструкция
1. Склонировать репозиторий
2. Добавить необходимые файлы для сортировки в директорию resource
3. Выполнить команду ./gradlew jar
4. Запустить jar командой java -jar build/libs/CftShiftTest-1.0.jar [ключи]

# Ключи
1. режим сортировки (-a или -d), необязательный, по умолчанию сортируем по возрастанию
2. тип данных (-s или -i), обязательный
3. имя выходного файла, обязательное
4. остальные параметры – имена входных файлов, не менее одного

Пример: java -jar build/libs/CftShiftTest-1.0.jar -a -s output.txt input1.txt input2.txt
