
#
## ∞ OnFleet Homework: Java Sample Makefile
#

# -- Config (Overridable via command line, hence the '?=')

TESTS ?= yes
TARGET ?= target
LOCAL_ENV ?= .env
GOAL ?= package

SURNAMES_FILE ?= https://raw.githubusercontent.com/enorvelle/NameDatabases/master/NamesDatabases/surnames/us.txt
FIRSTNAMES_FILE ?= https://raw.githubusercontent.com/enorvelle/NameDatabases/master/NamesDatabases/first%20names/us.txt
GEOJSON_BOUNDS ?= https://raw.githubusercontent.com/johan/world.geo.json/master/countries/USA/CA/San%20Francisco.geo.json

CODACY_API_TOKEN = naUJkcQpwRBDgJq8ooAU
CODACY_PROJECT_TOKEN = ed03e263fbff44d78353201a4f5cf840

CODACY_COMMAND ?= com.gavinmogan:codacy-maven-plugin:coverage -DcoverageReportFile=target/site/jacoco/jacoco.xml \
    -DprojectToken=$(CODACY_PROJECT_TOKEN) \
    -DapiToken=$(CODACY_API_TOKEN)


# -- Do not modify below this line

MAVEN = $(shell which mvn)
JAVA_PACKAGE = com.onfleet.demo.homework
STATIC_ANALYSIS_GOALS = pmd:pmd findbugs:findbugs
REPORTING_GOALS = jacoco:report

# use java package to calculate M2 path,
# see: https://www.gnu.org/software/make/manual/html_node/Text-Functions.html
# and: https://www.gnu.org/software/make/manual/html_node/Shell-Function.html
M2_ENV ?= "$(shell echo ~)/.m2/repository/$(subst .,/,$(JAVA_PACKAGE))"
MVN_OPTS ?=

ifneq ($(TESTS),yes)
MVN_OPTS += -DskipTests
endif


# -- Commands

all: help build  ## Install dependencies and build.

dependencies: $(LOCAL_ENV)  ## Install build tools.

docs: build  ## Build Javadocs and update the 'docs/' folder.
	@echo "Updating docs..."
	@cp -frv target/apidocs-private/apidocs/* docs/
	@echo "Docs updated."

install:  ## Build and install the OnFleet Homework code into the local Maven environment.
	@echo "Installing OnFleet Homework tool..."
	@mvn clean install $(MVN_OPTS)

release: clean install  ## Perform a full clean-build-report flow.
	@echo "Running reporting tools for Java..."
	@mvn $(STATIC_ANALYSIS_GOALS) $(REPORTING_GOALS)
	@mvn $(CODACY_COMMAND)

build: $(TARGET)  ## Build the OnFleet Homework code sample for Java.
	@echo "Build complete."

clean:  ## Clean ephemeral build targets.
	@echo "Cleaning build targets..."
	@rm -frv $(TARGET)

distclean: clean  ## Clean everything, including locally-cached dependencies.
	@echo "Cleaning Maven cache of Homework files..."
	@rm -frv $(M2_ENV)
	@echo "Resetting local environment..."
	@git reset --hard
	@git clean -xdf

help:  ## Display this documentation.
	@echo
	@echo "∞ onfleet - Java Homework Codesample"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
	@echo


# -- File Targets

$(TARGET): dependencies
	@echo "Building Java codesample for OnFleet Homework..."
	@mvn clean $(GOAL) $(MVN_OPTS)

$(LOCAL_ENV): $(LOCAL_ENV)/names/surnames.txt $(LOCAL_ENV)/names/firstnames.txt $(LOCAL_ENV)/geo/bounds.json
	@echo "Preparing development environment..."
ifeq ($(MAVEN),)
	@echo "Installing Maven..."
	@-brew install maven
endif

$(LOCAL_ENV)/names/surnames.txt $(LOCAL_ENV)/names/firstnames.txt $(LOCAL_ENV)/geo/bounds.json:
	@echo "Installing sample person name files..."
	@mkdir -p $(LOCAL_ENV)/names $(LOCAL_ENV)/geo
	@curl --progress-bar $(SURNAMES_FILE) > $(LOCAL_ENV)/names/surnames.txt
	@curl --progress-bar $(FIRSTNAMES_FILE) > $(LOCAL_ENV)/names/firstnames.txt
	@echo "Installing GeoJSON boundaries..."
	@curl --progress-bar $(GEOJSON_BOUNDS) > $(LOCAL_ENV)/geo/bounds.json

.PHONY: all dependencies build clean distclean help
