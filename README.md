# Assignment03
Conditions for valid mail file and how I read the file
1. Must be @enron for From:
2. Can not have <> bracket, meaning <.@enron.com> is invalid
3. Reads all lines after To:. Cc:, and Bcc: if there are still emails after the first line (only one needs to be present with an email for it to be a valid mail)
4. Any emails in these sections will be checked and if they do not have @enron, they will not be included since these sections include @enron and other emails without @enron

I believe these are the main conditions I have set for my reading. There is a high chance that this will lead to differences from expected results. I personally expect less received and team counts for each email. For amount of connectors, it can go either way.
