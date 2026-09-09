# Luna User Guide

Luna is a task manager that accepts commands through its chat interface. This
guide describes how to move tasks out of the active list while retaining them
in a local archive.

## Archive one task

Use `archive <index>` to archive the task at the displayed index.

For example:

```text
archive 2
```

Luna removes task 2 from the active list, renumbers the remaining tasks, and
appends the archived task to `data/archive.txt`.

```text
Archived 1 task to data/archive.txt.
Now you have 2 tasks in the list.
```

The index must be a positive integer that exists in the active task list.

## Archive all active tasks

Use `archive all` to move every active task into the archive.

```text
archive all
```

```text
Archived 3 tasks to data/archive.txt.
Now you have 0 tasks in the list.
```

If there are no active tasks, Luna does not modify the archive:

```text
There are no tasks to archive.
```

## List archived tasks

Use `list archived` to display archived tasks from oldest to newest.

```text
list archived
```

```text
Here are your archived tasks:
1. [T][ ] read book
2. [D][X] submit report (by: 12 Sep 2026)
```

If the archive is empty or does not exist:

```text
There are no archived tasks.
```

Archived tasks are read-only. They cannot be marked, unmarked, deleted,
searched, or restored from within Luna.

## Archive storage

Luna stores active tasks in `data/luna.txt` and archived tasks in
`data/archive.txt`. Both files use the same task-record format:

```text
T | 0 | read book
D | 1 | submit report | 2026-09-12
E | 0 | team meeting | 2026-09-12T09:00 | 2026-09-12T10:00
```

The archive is append-only, preserves archive order, and allows duplicate
entries. No archive timestamps are stored.

The existing `delete <index>` command remains a permanent deletion and does
not add the task to the archive.
