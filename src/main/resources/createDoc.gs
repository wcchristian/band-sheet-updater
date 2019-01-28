function createAndLinkLyrics(filename, lyrics, lyricFolderId, sheetId, destinationCell) {
    // copy the file url to the google sheet.
    var bandSheet = SpreadsheetApp.openById(sheetId)
    var range = bandSheet.getRange(destinationCell)

    if(lyrics.length > 0) {
        // Create file and move to lyrics area
        var doc = DocumentApp.create(filename);
        var lyricFile = DriveApp.getFileById(doc.getId());
        DriveApp.getFolderById(lyricFolderId).addFile(lyricFile);
        DriveApp.getRootFolder().removeFile(lyricFile);

        // add the lyrics to the doc
        var body = doc.getBody()
        for(var i=0; i<lyrics.length; i++) {
            body.appendParagraph(lyrics[i]);
        }

        range.setValue(lyricFile.getUrl())

    } else {
        range.setValue("No Lyrics Found")
    }
}