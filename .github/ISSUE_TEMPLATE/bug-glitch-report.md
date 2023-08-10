name: Bug/Glitch report
about: Follow this template if you want support/fix to be faster
title: "[Bug/Glitch]"
labels: bug
body:
  - type: markdown
    attributes:
      value: |
        Please fill in the following template to report your issue.
  - type: input
    id: mod-version
    attributes:
      label: "Better Furnaces Reforged Version"
      description: "The specific used mod version"
      placeholder: "Example 1.20.1-1.0.1"
    validations:
      required: true
  - type: input
    id: mod-loader-version
    attributes:
      label: Mod Loader Version
      placeholder: "Example: Forge/NeoForged 47.1.43 or Fabric Loader 0.14.21"
    validations:
      required: false
 
  - type: markdown
    attributes:
      value: "## Description"

  - type: textarea
    id: bug-context
    attributes:
      label: Bug/Glitch Occurrence Context
    validations:
      required: true
  
  - type: input
    id: crash-report
    attributes:
      label: Crash Report
      description: Paste here the link(Ex: Pastebin or mclo.gs) or the specific part of the issue if you are sure
    validations:
      required: false

    - type: textarea
    id: whw
    attributes:
      label: What happened wrong?
    validations:
      required: true
